# Offline Trainer
This project is an offline training and testing module for OpenCV Support Vector Machines. Input image datasets can be classified and compiled into output SVM model files for use with the Sign Language app:

* https://github.com/Mquinn960/sign-language

![Alt text](/Preview.png?raw=true "Preview")

## For a comprehensive step by step guide on using these applications and some additional info on how they work, please see [my new help repo](https://github.com/Mquinn960/sign-language-help)

## Getting Started

* Clone the repo onto your local machine by using:

    HTTPS: ```git clone https://github.com/Mquinn960/offline-trainer.git```
    
    SSH: ```git clone git@github.com:Mquinn960/offline-trainer.git```
   
* Ensure the prerequisites below are installed/satisfied

* *(Optional) Add your email credentials to the EmailNotifier class if you wish to receive SMTP emails regarding the training progress*

* Edit the Main method to include/exclude the training parameters you wish as outlined in the *user guide* below

* Method 1 - Manual Classpath
    * Add the following to your ```PATH``` under [environment variables](https://www.java.com/en/download/help/path.xml) in windows: ```F:\Repos\offline-trainer\new``` but substitute for the path to which you have cloned the repo on your computer
    * Load the project in IntellIJ IDEA, load the project and hit run

* Method 2 - Build artifact and command line
    * If you want to run the project from the command line you can elect to ```Build->Build Artifact``` which will produce an executable ```.jar``` in the ```offline-trainer\out\artifacts\``` folder
    * Run this ```.jar``` file from the command line using ```java -Djava.library.path=F:\Repos\offline-trainer\new\ -jar offline-trainer.jar``` but substitute the file path to your cloned repo in the ```java.library.path``` string

* Find the trained SVM XML files and the result logs in the folders specified

### Prerequisites

* Install [Java JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* Install [Maven](https://maven.apache.org/)
* Attain or create machine learning input images for the Sign Language you're attempting to model (see the User Guide below)
* It is recommended that you use the IntelliJ IDEA IDE noted below

### Development Environment

* Created using [IntelliJ IDEA](https://www.jetbrains.com/idea/)

## User Guide

### For a comprehensive step by step guide on using these applications and some additional info on how they work, please see [my new help repo](https://github.com/Mquinn960/sign-language-help)

This project takes in input images named by their class as the first filename letter from "train" and "test" folders within the main dataset folder itself. To easily create these datasets you can use the Dataset Creator python app with your webcam of choice:

* https://github.com/Mquinn960/dataset-creator

An example valid folder structure of multiple datasets can be found below. If you are looking for a good starting place to compile a dataset, please see the MNIST ASL Alphabet Sign Language dataset **[MNIST Sign Language Dataset](https://www.kaggle.com/datamunge/sign-language-mnist)**

```
/dataset1
  |
  train
      |---A_1_0.png
      |---A_1_1.png
      …
      |---Z_1_98.png
      |---Z_1_99.png
  |
  test
      |---A_2_0.png
      |---A_2_1.png
      …
      |---Z_2_98.png
      |---Z_2_99.png
/dataset2
  |
  train
      |---A_1_0.png
      …
  // etc.
```
* Aim for a training data to testing ratio of 80:20 - i.e. if you have 100 training images per class, then you should have at least 20 testing images
* Make these datasets available in a folder somewhere, and edit the Main method in the Offline Trainer Java code to specify where you saved them
* In this same method, specify a results output folder for the resultant log and confusion matrix data to be saved
* Again from the main method, update the 4 array parameters:
  * Detection Method - This should match the Feature Detection method specified in the Sign Language app itself (Contour Mask, Canny Edges, Skeleton)
  * Kernels - A list of available SVM kernels (Linear, RBF)
  * Dimensionality Reduction Type - Any dimensionality reduction techniques to employ (None, PCA)
  * Datasets - Input datasets you have produced (Add strings corresponding to any top level dataset folders in your dataset root folder in the format shown above)
* If you want to train a simple combination of parameters, remove everything else. Otherwise every combination of parameters will be trained and tested.
* Run the main project once this data is in place, the output ```trained.xml``` files will be output to the project root directory
* You can now use the trained SVM xml file with the Sign Language app (see repo in header) to test your model on your live Android device
* To update or change the imaging kernel to use your own feature detection parameters, see the section below on exporting the imaging library from the Sign Language app

## Changing the imaging kernel

The Offline Trainer application uses the exported imaging library of the Sign Language app. This is so that the trained SVM will have been created using an emulated version of what the Sign Language app's camera sees. If you update your Sign Language app fork to change the feature detection method, for example, you need to build and export its imaging library, and import it into the Offline Trainer. For steps on how to export the library, please see the Sign Language app repo below. The exported library ```app-release-null.jar``` should be copied into ```offline-trainer\new```

* https://github.com/Mquinn960/sign-language

## Built With

* [Java 8](https://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html) - Java Programming Language
* [Maven](https://maven.apache.org/) - Java package manager
* [OpenCV](https://opencv.org/) - The Open Source Computer Vision Library 

## Contributing

* Feel free to submit issues to this repository but please include usable information if you are looking to have something fixed
* Use [Feature Branching](https://www.atlassian.com/git/tutorials/comparing-workflows/feature-branch-workflow) where possible
* Submit Pull Requests to @Mquinn960 for review

## Authors

* **[Matthew Quinn](http://mquinn.co.uk)**

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
