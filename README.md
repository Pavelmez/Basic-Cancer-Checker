---

# Basic Cancer Checker

Basic Cancer Checker is an Android application built using Kotlin that allows users to check their skin for cancer. The app provides several features to help users monitor their skin health and stay informed about the latest news related to skin care and cancer prevention.

## Features

- **Skin Cancer Check**: Utilize the embedded machine learning model to check if a skin lesion is potentially cancerous.
- **History**: View a history of previous skin checks with the results.
- **News Articles**: Stay updated with the latest news articles related to skin health. Articles can be viewed and clicked for more details using the [News API](https://newsapi.org/).

## Installation

To run this project, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/skin-cancer-detector.git
   cd skin-cancer-detector
   ```

2. **Open in Android Studio**: Open the project in Android Studio.

3. **Build the project**: Let Gradle sync and build the project.

4. **Run the app**: Connect an Android device or start an emulator, then run the app.

## Usage

1. **Skin Check**: On the main screen, use the camera to take a picture of your skin. The embedded model will analyze the image and provide a result.
2. **View History**: Access your previous skin check results from the history screen.
3. **Read News Articles**: Browse through the latest news articles related to skin health and click on an article to read more.

## Development

### Prerequisites

- Android Studio
- Kotlin
- News API Key (You can obtain it from [News API](https://newsapi.org/))

### Configuration

1. **API Key**: Add your News API key to the `build.gradle.kts` file:
   ```properties
   buildConfigField("String", "NEWS_API_KEY", "\"your_api_keys\"")
   ```

2. **Dependencies**: The project uses several libraries, including:
   - Retrofit for API calls
   - Glide for image loading
   - Room for local database
   - ViewModel and LiveData for MVVM architecture
   - TensorFlow Lite or another ML library for the embedded skin cancer detection model

### Project Structure

- `retrofit` - Contains the Retrofit API service for fetching news articles.
- `database` - Contains the Room database and DAO interfaces.
- `view` - Contains the UI components like Activities, Fragments, and ViewModels.
- `helper` - Utility classes and helper functions.

### Contributing

Feel free to contribute to this project by submitting a pull request. Please follow the standard GitHub flow:
1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Push to the branch.
5. Open a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [News API](https://newsapi.org/)
- [Retrofit](https://square.github.io/retrofit/)
- [Glide](https://github.com/bumptech/glide)
- [Room](https://developer.android.com/training/data-storage/room)
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [TensorFlow Lite](https://www.tensorflow.org/lite) or other ML library used for skin cancer detection model

---
