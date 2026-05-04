# SporeX

**Mobile Mold Detection, Scan History, Community Support and Environmental Awareness App**

![Android](https://img.shields.io/badge/Android-Kotlin-green)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue)
![Backend](https://img.shields.io/badge/Backend-FastAPI-orange)
![Database](https://img.shields.io/badge/Database-MongoDB-brightgreen)
![API](https://img.shields.io/badge/API-Retrofit-purple)
![Deployment](https://img.shields.io/badge/Hosted%20On-Render-black)

---

## Table of Contents

- [Project Overview](#project-overview)
- [App Preview](#app-preview)
- [What SporeX Does](#what-sporex-does)
- [Main Features](#main-features)
- [Tech Stack](#tech-stack)
- [Architecture Overview](#architecture-overview)
- [Repository Structure](#repository-structure)
- [Local Setup](#local-setup)
- [Backend Setup](#backend-setup)
- [Android App Setup](#android-app-setup)
- [API Connection](#api-connection)
- [Testing](#testing)
- [Deployment](#deployment)
- [Known Setup Notes](#known-setup-notes)
- [Project Team](#project-team)

---

## Project Overview

**SporeX** is a mobile application designed to help users identify and manage possible mold problems in indoor environments.

The app focuses on giving users a simple way to:

- scan or upload mold-related images
- view scan history
- manage previous cases
- interact with a community page
- create and reply to posts
- manage user settings
- control app personalisation options
- connect with backend data stored through MongoDB

The project was developed as part of the final year project for **Team Ozone**. The system combines an Android mobile app with a backend API and database support.

---

## App Preview

### Authentication Screens

| Login | Register |
|---|---|
|<img width="360" height="787" alt="image" src="https://github.com/user-attachments/assets/e81971ae-924f-4c14-b5f3-529b907833c8" />|<img width="360" height="783" alt="image" src="https://github.com/user-attachments/assets/59070167-3de2-4e34-a228-88ca82dfd6ed" />|

### Main App Screens

| Home | Profile | Settings |
|---|---|---|
| <img width="448" height="973" alt="image" src="https://github.com/user-attachments/assets/a9eb65b2-2e27-44a2-88cb-636e87eca762" />|<img width="448" height="973" alt="image" src="https://github.com/user-attachments/assets/2bf9e7dd-b99c-4649-bbeb-d04b687c246a" />|<img width="448" height="973" alt="image" src="https://github.com/user-attachments/assets/7a76246a-700a-4a73-91f0-2d92e8dcfaf6" />|


### Mold Scan and History

| Scan Page | Scan Result | History |
|---|---|---|
|<img width="364" height="780" alt="image" src="https://github.com/user-attachments/assets/5206aaa2-b835-4720-955c-22fbc8fa8e89" />|<img width="363" height="785" alt="image" src="https://github.com/user-attachments/assets/2c6c9f74-347f-4b57-a9a6-98ad85d338fa" />| <img width="362" height="785" alt="image" src="https://github.com/user-attachments/assets/1081069e-1fdb-4ab4-aaae-97281bb35237" />|


### Community Page

| Community Feed | Create Post | Replies |
|---|---|---|
|<img width="362" height="784" alt="image" src="https://github.com/user-attachments/assets/036bb75e-a86a-4975-a366-3096a08e4e4a" />|<img width="366" height="786" alt="image" src="https://github.com/user-attachments/assets/73d79faf-b89e-411b-b251-b32668b16901" />| <img width="357" height="786" alt="image" src="https://github.com/user-attachments/assets/5a650688-7c31-438d-9461-b0dd2ba8a770" />|


---

## What SporeX Does

SporeX is built around the idea of helping users deal with mold issues earlier and in a more organised way.

The app gives users a place to scan or record possible mold cases, keep a history of previous scans, adjust privacy and personalisation settings, and use a community page to share posts or ask questions.

The system is not only focused on the visual side of mold detection. It also supports the wider project idea of environmental awareness, where humidity, temperature and device data can help explain why mold risk may be increasing.

---

## Main Features

### User Authentication

Users can create an account and log in through the Android app. Login details are sent to the backend using Retrofit API calls. When a login is successful, user details such as email and username are saved locally so they can be reused in the app session. The current app uses a hosted Render backend URL through the Retrofit client.

Main authentication features include:

- user registration
- user login
- local session storage
- username and email persistence
- login error handling
- redirect to main app after successful login

---

### Profile and Account Management

The profile area gives users access to key parts of the app, including:

- Community
- History
- My Device
- Settings

The profile screen also allows the user to open the edit profile page and update displayed account details.

Account management includes:

- edit profile
- change password screen
- delete account confirmation
- account options page
- navigation back to login after account deletion

The account page includes a delete confirmation step where the user must type `DELETE` before continuing.

---

### Settings and Personalisation

SporeX includes a settings area where users can manage app preferences.

Current settings include:

- dark mode
- notifications
- data personalisation
- app customisation
- account settings

The settings model supports `dark_mode`, `notifications_enabled`, `data_personalisation`, and an `app_customisation` map.

Dark mode can also be saved locally using shared preferences so the app remembers the user's choice.

---

### Scan History

The scan history feature allows users to keep track of previous mold scan records.

This helps the user:

- review previous mold cases
- check when scans were created
- separate recent and older scan records
- manage previous results
- delete scan history cases where needed

The history feature is important because mold problems often need to be monitored over time rather than checked only once.

---

### Community Page

The community feature allows users to interact with other users through posts and replies.

Community functionality includes:

- viewing community posts
- creating posts
- replying to posts
- connecting posts to backend data
- deleting posts where allowed
- showing delete options only for the correct user

This feature supports the wider aim of SporeX by giving users a place to ask questions, share mold-related experiences and get support.

---

### Device and Environmental Support

SporeX also includes support for device-related features.

The wider project includes environmental awareness, where sensor readings such as humidity, temperature and CO2 can help users understand indoor mold risk.

Possible environmental data includes:

- humidity
- temperature
- CO2
- device timestamp
- stored device readings
- mold risk context

This connects to the larger project goal of helping users prevent mold, not just identify it after it appears.

---

## Tech Stack

### Mobile App

- Android Studio
- Kotlin
- Jetpack Compose
- Material 3
- Retrofit
- Gson Converter
- OkHttp Logging Interceptor
- SharedPreferences
- Android Instrumentation Tests
- Compose UI Tests

### Backend

- Python
- FastAPI
- REST API endpoints
- Hosted backend on Render
- JSON request and response structure
- API testing through Postman

### Database

- MongoDB Atlas
- Collections for users, posts, replies, scan history, settings and related app data

### Testing Tools

- Android Studio test runner
- Jetpack Compose UI testing
- Postman
- Postman automated/scheduled tests
- Backend endpoint testing
- Manual user testing

---

## Architecture Overview

```text
Android App
    |
    | Retrofit API Calls
    |
FastAPI Backend
    |
    | Reads and writes data
    |
MongoDB Atlas Database
