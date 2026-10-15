# Proyecto AKU — Etapa 1

## Contenido
- `app/`: proyecto Android Kotlin + Gradle.
- `sandbox-html/index.html`: simulador visual de navegador; no controla el teléfono real.

## Abrir en Android Studio
1. Descomprimí `AKU.zip`.
2. Android Studio: **Open** y elegí la carpeta `AKU` (la que contiene `settings.gradle.kts`).
3. Esperá la sincronización de Gradle.
4. Conectá el teléfono por USB y habilitá Depuración USB.
5. Ejecutá con Run.

## Importante
Este paquete contiene el código fuente entregado y organizado; no es un APK ya compilado ni garantiza que compile sin ajustes. El proyecto usa Android Gradle Plugin 8.2.0, Kotlin 1.9.20 y SDK 34. Asegurate de tener instalado el SDK 34 y un JDK compatible en Android Studio.

El servicio de accesibilidad puede leer elementos de la pantalla y ejecutar gestos. Activarlo concede permisos sensibles. Usalo solo en tu dispositivo y revisá el comportamiento antes de confiarle acciones.

La búsqueda de texto es una implementación inicial: puede escribir en un campo editable, pero no necesariamente tocar el botón de búsqueda ni esperar a que la app termine de abrir.
