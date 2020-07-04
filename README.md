# Malaria-Detection-using-Image-Processing-and-Transfer-Learning.

The project's main aim was to design a method which is more faster and portable way to detect malaria in blood cells even when there is no expert present. So to do this we used blood cell dataset to train our Transfer learning VGG-16 CNN model.The training even involved the use of stain normalization to clear the image of noise. 

The project consists of three main modules:
             1)The image processing and deep learning model.
             2)The mobile app
             3) The database API to store the results.

The image processing and deep learning model module is implemented using python deep learning library tensorflow and imageo image processing library. The malaria predictor model takes an image input, processes it and passes it on to the neural network and then prediction is made. This model is trained on blood cell images dataset and pickled. The pickled ml model is hosted using flask and
ngrok-server on google co-lab.

The mobile is developed using Java on Android studio. Firstly, in the mobile app the user has to select a captured blood cell image. Then using the okhttp library provided by Java for Android, the image is sent in a post request to the image processing and deep learning module (hosted on ngrok-server). The ml model processes the image and predicts whether blood cell is affected or not, then
the result is sent as response to the android app. In the android app there is also option to save the image + result in database as a record.

In order for the Android app to interact with database, there is a database API module. The API is used to save image + prediction records in postgreSQL database, also it is used to view past prediction records. The API is written using Java Servlets and uses Hibernate ORM to interact with the postgreSQL database. The Android app interact with the database API module using okhttp library provided by Java for Android.
