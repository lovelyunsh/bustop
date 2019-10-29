import RPi.GPIO as GPIO
import time
from firebase import firebase

GPIO.setmode(GPIO.BOARD)

fb = firebase.FirebaseApplication('https://bustop-bc098.firebaseio.com/', None)

LED = 11
GPIO.setup(LED, GPIO.OUT, initial = 0)
while True :
    if fb.get('/led',"led") == 1 :
        GPIO.output(LED, 1)
        while True:
            if fb.get('/led',"led") == 0 :
                GPIO.output(LED, 0)
                break
GPIO.cleanup()
