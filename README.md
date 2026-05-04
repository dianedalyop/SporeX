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


Android App
    |
    | Retrofit API Calls
    |
FastAPI Backend
    |
    | Reads and writes data
    |
MongoDB Atlas Database

## How It Works

1. The user registers or logs into the Android app.
2. The app stores basic session details locally, such as username and email.
3. Retrofit sends requests from the Android app to the FastAPI backend.
4. The backend handles requests such as login, register, posts, replies, settings and history.
5. MongoDB stores the project data.
6. The app displays the returned data inside Jetpack Compose screens.
7. Users can manage their profile, view history, use community posts and adjust settings.

---

## Repository Structure

The exact structure may change as development continues, but the project is generally organised like this:

```text
SporeX/
|-- app/                         # Android mobile application
|   |-- src/
|   |   |-- main/
|   |   |   |-- java/com/example/sporex_app/
|   |   |   |   |-- network/      # Retrofit client, API models and requests
|   |   |   |   |-- useraccount/  # Login, register, profile, settings and account screens
|   |   |   |   |-- ui/           # Compose UI screens and navigation
|   |   |   |   |-- utils/        # Utility functions such as dark mode helpers
|   |   |-- androidTest/         # Android instrumentation and Compose UI tests
|   |   |-- test/                # Unit tests
|
|-- backend/                     # FastAPI backend, if stored in repo
|-- docs/
|   |-- images/                  # README screenshots
|-- README.md
```

---

## Local Setup

### Prerequisites

Before running the project locally, make sure you have:

- Android Studio installed
- Android SDK installed
- Kotlin support enabled
- Python installed for backend development
- MongoDB Atlas connection details
- Internet access for the hosted backend or database
- Git installed
- Postman installed for API testing

---

## Android App Setup

### 1. Clone the Repository

```bash
git clone <your-repo-url>
cd SporeX
```

### 2. Open in Android Studio

Open the project folder in Android Studio.

Allow Gradle to sync fully before running the app.

### 3. Check the Backend URL

The app uses Retrofit to connect to the backend.

The current Retrofit client uses the hosted Render backend:

```kotlin
private const val BASE_URL = "https://sporex.onrender.com/"
```

This is located in:

```text
app/src/main/java/com/example/sporex_app/network/RetrofitClient.kt
```

The Retrofit client also uses:

- OkHttp logging
- Gson converter
- `SporexApi` interface

---

## API Connection

### Hosted Backend

The deployed backend URL is:

```text
https://sporex.onrender.com/
```

Use this when testing the app without running the backend locally.

### Local Backend

If running the backend locally, change the Retrofit base URL.

For Android Emulator:

```kotlin
private const val BASE_URL = "http://10.0.2.2:5000/"
```

For a real phone on the same Wi-Fi:

```kotlin
private const val BASE_URL = "http://YOUR_LAPTOP_IP:5000/"
```

Example:

```kotlin
private const val BASE_URL = "http://192.168.1.90:5000/"
```

Make sure:

- the phone and laptop are on the same network
- the backend is running
- the firewall is not blocking the port
- the backend port matches the URL

---

## Backend Setup

Adjust this section depending on where the backend is stored in your repository.

### 1. Go to Backend Folder

```bash
cd backend
```

### 2. Create a Virtual Environment

```bash
python -m venv venv
```

Activate it on Windows:

```bash
venv\Scripts\activate
```

Activate it on macOS/Linux:

```bash
source venv/bin/activate
```

### 3. Install Requirements

```bash
pip install -r requirements.txt
```

### 4. Add Environment Variables

The backend may need environment variables such as:

```text
MONGO_URI=<your-mongodb-atlas-uri>
DATABASE_NAME=<your-database-name>
```

### 5. Run the Backend

```bash
python app.py
```

or, if using Uvicorn:

```bash
uvicorn app:app --reload --host 0.0.0.0 --port 5000
```

The backend should then run at:

```text
http://localhost:5000
```

---

## Main Backend Areas

### Authentication

- Register user
- Login user
- Store user details
- Return success or error messages

### Community Posts

- Create post
- Get all posts
- Get post by ID
- Add replies
- Delete post

### Scan History

- Store scan result
- Fetch user scan history
- Group history by date
- Delete history case

### Settings

- Fetch user settings
- Update dark mode
- Update notifications
- Update data personalisation preference

### Device Data

- Store device/environment readings
- Support mold risk context through environmental data

---

## Testing

Testing was an important part of the SporeX project.

The project includes several types of testing:

- frontend Android UI testing
- backend Postman testing
- endpoint response testing
- manual user testing
- automated Postman scheduled testing

---

## Android Tests

Android tests were created to check important screens and flows in the mobile app.

Examples of tested areas include:

- login screen
- register screen
- profile page
- community page
- mold result screen
- settings page
- navigation between screens

Run Android unit tests with:

```bash
./gradlew test
```

On Windows:

```bash
.\gradlew test
```

Run connected Android tests with:

```bash
./gradlew connectedAndroidTest
```

On Windows:

```bash
.\gradlew connectedAndroidTest
```

---

## Postman Tests

Postman was used to test backend API endpoints.

Tested areas include:

- register endpoint
- login endpoint
- get posts
- create post
- get post by ID
- add reply
- delete post
- settings endpoints
- scan history endpoints

Postman tests checked things such as:

- correct status codes
- valid JSON response
- response body structure
- success messages
- error handling
- response time
- required fields

Example Postman test checks:

```javascript
pm.test("Status code is successful", function () {
    pm.expect(pm.response.code).to.be.oneOf([200, 201]);
});

pm.test("Response is valid JSON", function () {
    pm.response.to.be.json;
});

pm.test("Response time is acceptable", function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});
```

---

## Automated API Testing

After the backend was deployed, automated API testing became easier because the app no longer depended only on a local backend.

Postman scheduled tests were used to run backend checks automatically and confirm that important endpoints were still working.

This helped with:

- checking deployed backend availability
- catching endpoint issues earlier
- keeping evidence for Mahara and project testing
- showing that testing was repeated, not just done once manually

---

## Deployment

### Backend Deployment

The backend is deployed using Render.

Current hosted backend:

```text
https://sporex.onrender.com/
```

The Android app connects to this hosted backend through Retrofit.

### Database Hosting

MongoDB Atlas is used as the cloud database.

This allows the app and backend to access project data from the hosted environment.

---

## Known Setup Notes

- If the Android app cannot connect to the backend, check the Retrofit `BASE_URL`.
- For emulator testing, use `10.0.2.2` instead of `localhost`.
- For real phone testing, use the laptop's local Wi-Fi IP address.
- Make sure the backend port matches the URL used in the Android app.
- If using the Render backend, the first request may be slow if the service has been inactive.
- Do not commit private database connection strings.
- Do not commit secret keys or environment files.
- README images should be placed in `docs/images/` so GitHub can display them correctly.
- If frontend tests fail after pulling new code, check whether screen text, content descriptions or variable names were changed.

---

## Project Team

**Team Ozone**

| Team Member | Role |
|---|---|
| Wiktor Teter | Team Lead, Lead Tester, Database Operations |
| Meghan Keightley | Creative Lead, UX/UI Designer, Frontend Operations |
| Xu Teck Tan | Lead Hardware and Security |
| Eljesa Mesi | Lead Scrum Master, Overall Support |
| Diane Jugul Dalyop | Backend Functionality and Operations, Team Researcher |

---

## Closing Note

SporeX brings together mold scanning, scan history, user settings, community support and environmental awareness into one mobile app.

The main goal of the project is to help users identify possible mold issues earlier, understand their indoor environment better, and keep their mold-related information organised in one place.

This repository shows the Android application, backend API connection, database-supported features and testing work completed as part of the final year project.
