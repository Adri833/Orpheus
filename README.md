# 🎵 Orpheus

Orpheus es un reproductor de música local desarrollado en **Kotlin** utilizando **Jetpack Compose**.  
La aplicación está pensada para ser **escalable, mantenible y eficiente**, aplicando principios de **Clean Architecture** y el patrón **MVVM**.

Incluye reproducción en segundo plano mediante **Foreground Service**, notificaciones persistentes y una gestión avanzada de la cola de reproducción.

## 🚀 Funcionalidades principales

- 🎶 Reproducción de música local
- ▶️⏸️ Control completo de reproducción (play, pausa, siguiente, anterior)
- 🔀 Gestión avanzada de cola de reproducción
- 🔁 Modo **shuffle**
- 📌 Reproducción en segundo plano
- 🔔 Notificaciones persistentes con controles multimedia
- 📂 Lectura de canciones desde **MediaStore**

<p align="center">
  <img src="screenshots/Home.jpg" width="250" style="margin-right: 16px;" />
  <img src="screenshots/Starboy.jpg" width="250" />
</p>

## 🧠 Arquitectura

La aplicación sigue una arquitectura **MVVM + Clean Architecture**, separando claramente responsabilidades:

- **UI**: Jetpack Compose
- **ViewModel**: Gestión del estado y lógica de presentación
- **Domain**: Casos de uso
- **Data**: Repositorios y fuentes de datos
- **PlayerManager**: Gestión centralizada del estado de reproducción

## 🛠️ Tecnologías utilizadas

- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose
- **Arquitectura**: MVVM, Clean Architecture
- **Reproducción**: ExoPlayer
- **Inyección de dependencias**: Hilt
- **Servicios**: Foreground Service
- **Gestión multimedia**: MediaStore
