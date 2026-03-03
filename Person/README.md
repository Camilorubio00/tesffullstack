# Frontend Personas (Android)

Aplicación Android en **Kotlin + Jetpack Compose** que consume un backend REST de personas y resuelve la prueba técnica solicitada:

- **Pantalla Home** con listado de personas, botón **Crear**, y acciones de **Editar** y **Borrar** por registro.
- **Pantalla Formulario** para **crear** o **editar** una persona, con validaciones básicas y selección de fecha amigable.

### Requisitos

- Android Studio Jellyfish (o superior).
- JDK 11.
- Backend de personas corriendo en el host en `http://localhost:8080`.

> En el **emulador de Android**, el `localhost` del host se accede como `http://10.0.2.2`.  
> Por eso, en `RetrofitConstants.kt` se usa `BASE_URL = "http://10.0.2.2:8080/"`.

---

## Arquitectura general

- **Patrón por capas**:
  - **UI (Compose)**: pantallas `HomeScreen` y `PersonFormScreen`.
  - **ViewModels**: `HomeViewModel`, `PersonFormViewModel`.
  - **Dominio (casos de uso)**:
    - `FetchPersonListUseCase`
    - `FetchPersonByIdUseCase`
    - `CreatePersonUseCase`
    - `UpdatePersonByIdUseCase`
    - `DeletePersonByIdUseCase`
  - **Data**:
    - `PersonApi` (Retrofit)
    - `PersonRemoteDataSource`
    - `PersonRepository`
    - DTOs y mapeos `PersonDto` ↔ `Person`.

- **Inyección de dependencias**:  
  - `App.kt` anotada con `@HiltAndroidApp`.  
  - `MainActivity` anotada con `@AndroidEntryPoint`.  
  - Módulo `MainActivityModule` configura:
    - `OkHttpClient` (timeouts).
    - `Retrofit` con `BASE_URL` y `GsonConverterFactory`.
    - `ApiExceptionHandler`.
    - `CoroutinesDispatchers`.

- **Navegación sencilla propia**:
  - `MainActivity` mantiene un `sealed class Screen { Home, Form(personId) }`.
  - `PersonApp()` cambia entre `HomeScreen` y `PersonFormScreen` según el estado actual.
  - No se usa Navigation Compose para mantener el código simple y explícito.

---

## Pantalla Home (listar y borrar)

- **Archivo**: `HomeScreen.kt`
- **ViewModel**: `HomeViewModel`
  - Inyecta `FetchPersonListUseCase` y `DeletePersonByIdUseCase`.
  - Estado (`HomeUiState`):
    - `isLoading`
    - `persons: List<Person>`
    - `errorMessage`
  - `loadPersons()`:
    - Llama a `FetchPersonListUseCase()` y actualiza la lista.
  - `deletePerson(id)`:
    - Llama a `DeletePersonByIdUseCase(currentList, id)` para que el caso de uso construya la nueva lista sin volver a pedir todo al backend.

- **UI**:
  - AppBar con título **“Personas”**.
  - Botón **Crear** (callback `onCreateClick`) que navega al formulario vacío.
  - `LazyColumn` con cada `Person` mostrando:
    - Nombre + apellido.
    - Fecha de nacimiento.
    - Puesto.
    - Sueldo.
    - Botones **Editar** y **Borrar**.

---

## Pantalla Formulario (crear / editar)

- **Archivos**: `PersonFormScreen.kt`, `PersonFormViewModel.kt`, `ui/model/PersonFormUiState.kt`.

- **ViewModel**: `PersonFormViewModel`
  - Inyecta:
    - `FetchPersonByIdUseCase`
    - `CreatePersonUseCase`
    - `UpdatePersonByIdUseCase`
  - Estado (`PersonFormUiState`):
    - `personId`
    - `name`, `lastName`, `birthdate`, `job`, `salaryText`
    - `isLoading`, `isEditMode`, `errorMessage`
  - Responsabilidades:
    - `loadPerson(id)`:
      - Llama a `FetchPersonByIdUseCase(id)` y mapea el resultado a los campos del formulario.
    - `resetForCreate()`:
      - Limpia el estado para modo creación.
    - `onBirthdateChangeFromPicker(year, month, day)`:
      - Mapea la fecha del `DatePickerDialog` a formato `YYYY-MM-DD` (ej. `1995-04-30`).
    - `submit(onFinished)`:
      - Valida que no haya campos vacíos y que `salaryText` sea numérico.
      - Llama:
        - `UpdatePersonByIdUseCase` si `isEditMode`.
        - `CreatePersonUseCase` si es nuevo.
      - Expone solo éxito/fracaso y mensajes de error a la UI.

- **Validaciones en la vista (no en el ViewModel)**:
  - Cada `OutlinedTextField` aplica límites de caracteres:
    - `name`, `lastName` → hasta 40 caracteres.
    - `job` → hasta 60 caracteres.
  - El campo `Sueldo`:
    - Filtra en la vista para permitir sólo dígitos y un único punto decimal, longitud limitada.
    - El valor filtrado se pasa al ViewModel (`salaryText`), que luego lo convierte a `Double` en `submit`.

- **Selección de fecha**:
  - Botón **“Elegir fecha”** abre un `DatePickerDialog` nativo.
  - El resultado se convierte automáticamente al string `YYYY-MM-DD` y se muestra en un campo de texto de solo lectura.

---

## Capa de datos y endpoints

- **Archivo**: `data/PersonApi.kt`
  - `GET /api/personas` → `fetchPersonList()`
  - `POST /api/personas` → `createPerson(@Body PersonDto)`
  - `PUT /api/personas/{id}` → `updatePersonBy(@Path("id") id, @Body PersonDto)`
  - `GET /api/personas/{id}` → `fetchPersonBy(@Path("id") id)`
  - `DELETE /api/personas/{id}` → `deletePersonBy(@Path("id") id)`

- **Modelo remoto y mapeos** (`data/model/PersonDto.kt`):
  - DTO con nombres en español (`nombre`, `apellido`, etc.).
  - Funciones de extensión para convertir a `domain/model/Person` y viceversa.

- **Constantes de red** (`data/RetrofitConstants.kt`):
  - `BASE_URL = "http://10.0.2.2:8080/"`
  - `PERSON = "api/personas"`

- **Permisos y cleartext** (`AndroidManifest.xml`):
  - `INTERNET` habilitado.
  - `android:usesCleartextTraffic="true"` en la etiqueta `application` para permitir `http://` hacia el backend local.

---

## Cómo ejecutar el backend

1. Levanta tu backend en el puerto **8080** del host (por ejemplo `http://localhost:8080`).
2. Verifica que el endpoint `http://localhost:8080/api/personas` responda correctamente (por ejemplo con Postman o navegador).
3. No cambies nada en Android: el emulador ya usa `10.0.2.2` internamente para llegar al `localhost` del host.

---

## Cómo ejecutar la app en el emulador

1. Abre el proyecto en Android Studio.
2. Espera a que termine la sincronización de Gradle (Hilt, Retrofit, Compose).
3. Selecciona un dispositivo virtual (AVD) con Android 8.0+.
4. Ejecuta la app con **Run > Run 'app'**.

### Flujo funcional completo

- **Home**:
  - Al abrir, llama a `FetchPersonListUseCase` y muestra todas las personas.
  - Botón **Crear** → navega al formulario en modo creación.
  - Cada registro:
    - Botón **Editar** → formulario con los datos cargados (usa `FetchPersonByIdUseCase`).
    - Botón **Borrar** → llama a `DeletePersonByIdUseCase` y actualiza la lista en memoria.

- **Formulario**:
  - En modo edición:
    - Carga inicial con `FetchPersonByIdUseCase`.
    - Valores actuales se muestran en cada campo.
  - En modo creación:
    - Campos vacíos.
  - Botones:
    - **Guardar**:
      - Valida campos obligatorios y salario.
      - Llama a `CreatePersonUseCase` o `UpdatePersonByIdUseCase`.
      - Si todo va bien, vuelve a Home (callback `onSaved`).
    - **Cancelar**:
      - Vuelve a Home sin cambios.

---

## Tests incluidos

- **Prueba de mapeo DTO → dominio**  
  - Archivo: `app/src/test/java/com/example/person/ExampleUnitTest.kt`
  - Verifica que un `PersonDto` se convierta correctamente a `Person` (`id`, `nombre`, `apellido`, `fechaNacimiento`, `puesto`, `sueldo`).

Para ejecutarla:

1. En Android Studio abre la ventana **Run / Tests**.
2. Ejecuta los tests del módulo `app` o la clase `ExampleUnitTest`.

---

## Capturas de pantalla sugeridas

Crea un directorio `screenshots/` en la raíz del proyecto y agrega, por ejemplo:

- `screenshots/listado_personas.png`
- `screenshots/formulario_crear_persona.png`
- `screenshots/formulario_editar_persona.png`

Incluye estas imágenes en el repositorio junto al código en la rama requerida (`apellido_frontend`).
