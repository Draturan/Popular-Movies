# Popular-Movies


[![GitHub license](https://img.shields.io/github/license/Draturan/Popular-Movies.svg)](https://github.com/Draturan/Popular-Movies/blob/master/LICENSE)
[![Platform](https://img.shields.io/badge/platform-Android-blue.svg)](https://www.android.com)
[![Language](https://img.shields.io/badge/language-Java-blue.svg)](https://www.android.com)
[![GitHub issues](https://img.shields.io/github/issues/Draturan/Popular-Movies.svg)](https://github.com/Draturan/Popular-Movies/issues)


This project is developed as an exercise for Udacity's Scholarship program (Nanodegree Android Developer).<br/>
The APP is made to discover recent movies and learn more about them.

This app utilizes `Core Android User Interface` components and fetches movie information using `themoviedb.org web API`.

<hr>

## Features

The App displays a scrolling grid of movie trailers, launches a details screen whenever a particular movie is selected, allows users to save favorites, play trailers, and read user reviews.

| Main screen | Favorites | Film Description | Film Description |
| ------- | ------ | ----- | ----- |
| <img src="https://raw.githubusercontent.com/Draturan/Popular-Movies/master/assets/1_MainActivity.png" width="200px" alt="Main Activity"/> | <img src="https://raw.githubusercontent.com/Draturan/Popular-Movies/master/assets/2_FavoritesActivity.png" width="200px" alt="Favorites Activity"/> | <img src="https://raw.githubusercontent.com/Draturan/Popular-Movies/master/assets/3_DescriptionActivity.png" width="200px" alt="Description Activity"/> | <img src="https://raw.githubusercontent.com/Draturan/Popular-Movies/master/assets/4_DescriptionActivity.png" width="200px" alt="Description Activity 2"/> |

| Landscape Mode |
|--------|
|<img src="https://raw.githubusercontent.com/Draturan/Popular-Movies/master/assets/5_LandscapeMode.png" width="500px" alt="Landscape Mode"/>|

## Build the App

1. Clone
```
git clone https://github.com/fjoglar/popular-movies-app.git
```
  or download this repository in your local machine.

2. Open it with Android Studio

3. Get a free developer API key from [The Movie Database](https://www.themoviedb.org/) website: this will be a your personal one, do not share it publicly.

4. Inside the `gradle.properties` file you will find `APY_KEY_TMDB` variable. Substitute his value with your API key.

```
# API Key from The Movies DataBase
APY_KEY_TMDB = "your_tmdb_api_key"
```

## Languages, libraries and tools used

* [Java](https://docs.oracle.com/javase/8/)
* Android Support Libraries
* [Picasso](https://github.com/square/picasso)
* [Butterknife](https://github.com/JakeWharton/butterknife)

## Requirements

* JDK 1.8
* [Android SDK](https://developer.android.com/studio/index.html)
* Android M ([API 23](https://developer.android.com/preview/api-overview.html))
* Latest Android SDK Tools and build tools.

## License

```

GNU General Public License v3.0

```
