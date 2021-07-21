import logging


class ErrorLogs:

        logging.basicConfig(filename='logs/errorLogs.log', filemode ='a',
        format='%(asctime) s %(name)s - %(levelname)s - %(message)s')
        logging.error("** LOG ERROR: The MySQL connection has failed:")



