<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#libraries">Libraries</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#project-overview">Project overview</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

<img src="https://user-images.githubusercontent.com/7949400/193445156-14faa4b6-1685-45b1-82ae-2d89dd4cb1e0.gif" width="400">

The concept of reactive programming and declarative programming is more and more popular in recent years, there are a lot of libraries to help you do that, like RxJava, LiveData and Coroutine Flow. In this demo app, I will show you how to build a modern reactive Android app.

<!-- LIBRARIES -->
### Libraries

The main purpose of this app is to demo reactive architecture as simple as possible, introduce too many libraries would make this demo app much harder to learn. So here I only use these four libraries:
* [Jetpack Compose](https://developer.android.com/jetpack/compose)
* [Android Architecture Component - ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [Firebase Cloud FireStore](https://firebase.google.com/products/firestore)
* [RxJava](https://github.com/ReactiveX/RxJava)



<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites

Android Studio preview: [https://developer.android.com/studio/preview](https://developer.android.com/studio/preview)


### Installation

1. Add a Firebase project and get `google-service.json` at [Firebase console](https://console.firebase.google.com/)
2. Clone the repo
   ```sh
   git clone https://github.com/hungyanbin/ResctiveStickyNote.git
   ```
3. Copy `google-service.json` to this folder: `ReactiveStickyNote/app/`
4. Hit Run on Android Studio



<!-- PROJECT OVERVIEW -->
## Project overview

![Architecture image][architecture-image]

#### Topics
- Backpressure
- Multicasting
- Threading
- Binding
- Error handling


<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact


Yanbin Hung - [@YanbinHung](https://twitter.com/YanbinHung) - hungyanbin2@gmail.com

Project Link: [https://github.com/hungyanbin/ResctiveStickyNote](https://github.com/hungyanbin/ResctiveStickyNote)


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[architecture-image]: images/ReactiveArchitecture.png