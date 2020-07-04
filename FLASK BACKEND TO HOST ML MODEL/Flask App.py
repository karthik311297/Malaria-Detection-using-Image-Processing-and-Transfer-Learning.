# -*- coding: utf-8 -*-
"""Untitled0.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/1mr9a0AKv36N-Qf82DKP9NWBRk0IK9F9Q
"""

!pip install tensorflow==1.7

!pip install keras==2.2.4

!pip install staintools
!pip install spams

import os
import shutil
import numpy as np
import pandas as pd
import cv2
import imageio
from datetime import datetime as dt
import matplotlib.pyplot as plt
import random
from PIL import Image
import staintools

from keras.preprocessing.image import ImageDataGenerator,load_img,img_to_array,array_to_img
from keras.models import Sequential, load_model
from keras.layers import Conv2D, MaxPooling2D
from keras.layers import Activation, Dropout, Flatten, Dense, BatchNormalization
from keras import backend as K
from keras.optimizers import RMSprop
from keras import regularizers as reg
import numpy as np
import matplotlib.pyplot as plt
import os
import imageio
from keras import optimizers
from keras.callbacks import History
from keras import applications
from keras.utils.np_utils import to_categorical
import math  
import cv2

import tensorflow as tf

"""# New Section"""

# Commented out IPython magic to ensure Python compatibility.
# %tensorflow_version 1.x

graph = tf.get_default_graph()

def resize_image(image):
    resized_image = cv2.resize(image, (128,128), interpolation = cv2.INTER_AREA) #Resize all the images to 128X128 dimensions
    target = staintools.LuminosityStandardizer.standardize(resized_image)
    normalizer = staintools.StainNormalizer(method='vahadane')
    normalizer.fit(target)
    transformed = normalizer.transform(target)
    return transformed

!unzip -uq "/content/drive/My Drive/malaria.zip" -d "/content"

from google.colab import drive
drive.mount('/content/drive')

from google.colab import files



!pip install flask-ngrok

import werkzeug

import joblib

predictorModel=joblib.load("/content/malaria.pkl")



from flask_ngrok import run_with_ngrok

import flask

app=flask.Flask(__name__)

run_with_ngrok(app)

@app.route('/',methods=['GET','POST'])
def homeFunc():
    imagefile=flask.request.files['image']
    filename=werkzeug.utils.secure_filename(imagefile.filename)
    print("\n Received image File Name : " + imagefile.filename)
    imagefile.save("/content/CellImages/"+filename)
    image=imageio.imread("/content/CellImages/"+filename)
    
    resized=resize_image(image)
    imageio.imsave("/content/Resized/test.png",resized)
    test_datagen = ImageDataGenerator(rescale=1./255)
    test_generator=test_datagen.flow_from_directory(
        ".",classes=['Resized'],
        target_size=(128,128),
        color_mode='rgb',
        shuffle=True,
        class_mode='categorical',
        batch_size=1
    )
    predicted=[]
    with graph.as_default():
        predicted=predictorModel.model.predict_generator(test_generator)
    # os.remove("/content/Resized/test.png")
    if predicted.item(0)>predicted.item(1):
        print("INFECTED")
        os.remove("/content/Resized/test.png")        
        return "INFECTED"
    else:
        print("HEALTHY")
        os.remove("/content/Resized/test.png")                
        return "HEALTHY"

app.run()
