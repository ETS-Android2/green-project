#!/usr/bin/python3.9

from json.decoder import JSONDecodeError
import pymysql
import json
import logging
import cryptography
from logs.errorLogs import ErrorLogs

# from sys import path

# print(path)
# path.append('.')


class mysqlConnection:
    def __init__(self):
        self.getConnection()
        

    def getConnection(self):

        try:

            json_file = open("./mysql/mysql_credentials.json")

            credentials = json.load(json_file)

            if 'user' and 'password' and 'database' and 'host' in credentials:
                try:
                    username = credentials['user']
                    password = credentials['password']
                    database = credentials['database']
                    host = credentials['host']

                    db = pymysql.connect(
                        host=host,
                        user=username,
                        password=password,
                        database=database,
                        cursorclass=pymysql.cursors.DictCursor
                    )

                    self.db = db                    
                    print(" ** Connected to MySQl... ")
                except pymysql.Error as e:
                    ErrorLogs()

                self.cursor = db.cursor()

            else:
                raise Exception("The parameters required were not presented. [user, password]")
            

        except JSONDecodeError as e:
            print(" ** LOG ERROR: The credentials.json file is wrong: ", e)
        except FileNotFoundError as e:
            print(" ** LOG ERROR: The credentials.json was not found: ", e)
        except Exception as e:
            print(" ** LOG ERROR: Weird exception: ", e)

        return self.cursor

    def insert(self, SQL_string):
        try:

            print(" ** SQL Command: ", SQL_string)
            self.cursor.execute(SQL_string)
            self.db.commit()

        except pymysql.Error as e:
            print(" ** LOG ERROR: The MySQL INSERTION has failed: ", e)
    

    def consult(self, SQL_string):
        try:
            print(" ** SQL Command: ", SQL_string)
            self.cursor.execute(SQL_string)

            return self.cursor.fetchall()
            
        except pymysql.Error as e:
            return {
                'res' : 'Consult error: {}'.format(e),
            }

    def closeConnection(self):
        self.cursor.close()

            
            


    