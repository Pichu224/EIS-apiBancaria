# API Bancaria

Proyecto de ejemplo para una API bancaria con Spring Boot.

## Descripción

Esta aplicación expone endpoints básicos de usuarios, cajas, movimientos y transferencias. La capa de servicio se prueba con Mockito para evitar dependencia con la base de datos.

## Requisitos

- Java 21
- Gradle Wrapper (`gradlew` / `gradlew.bat`)
- PostgreSQL para ejecución real (solo si se usa la aplicación completa)

## Ejecución

Desde la raíz del proyecto:

```powershell
.\gradlew.bat bootRun
```

## Pruebas

Ejecutar la suite completa de pruebas unitarias:

```powershell
.\gradlew.bat test
```

## Qué se agregó

- `CHANGELOG.md` con los cambios realizados en esta iteración.
- `src/test/java/com/unq/eis/apibancaria/controller/UsuarioControllerTest.java` para verificar el comportamiento HTTP de `UsuarioController`.
- Documentación sobre cómo ejecutar y validar los tests.

## Notas

- Los tests de servicio ahora usan mocks para los DAOs y no requieren una base de datos en memoria.
- Solo `UsuarioController` tiene endpoints implementados en esta versión; los otros controladores están presentes como clases vacías.
