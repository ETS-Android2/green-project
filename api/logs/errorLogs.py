import logging


class ErrorLogs:

        def __init__(self,message):
                logging.basicConfig(filename='logs/errorLogs.log', filemode ='a',
                format='%(asctime) s %(name)s - %(levelname)s - %(message)s')
                logging.error(message)
                print(message)



