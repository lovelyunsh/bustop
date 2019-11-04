
import RPi.GPIO as GPIO
import time
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)
buzzer = 11
scale = [ 261, 294, 329, 349, 392, 440, 493, 523 ]
GPIO.setup(buzzer, GPIO.OUT)
GPIO.setup(sw, GPIO.IN) 
p = GPIO.PWM(buzzer, 100)

list = [0,0,4,4,5,5,4,3,3,2,2,1,1,0]
try:
    while True:
        if True: 
            print(1)
            p.start(100)
            time.sleep(5)
            print(2)
            p.ChangeDutyCycle(90) # dutycycle 변경
            time.sleep(5)
            print(2)
            for i in range(len(list)):
                p.ChangeFrequency(scale[list[i]])
                print(scale[list[i]])
                if (i+1)%7 == 0:
                    time.sleep(1)
                else :
                    time.sleep(0.5)
                    p.stop()
except KeyboardInterrupt:
    GPIO.cleanup()
