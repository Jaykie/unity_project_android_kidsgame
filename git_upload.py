#!/usr/bin/python
# coding=utf-8
import zipfile
import shutil
import os
import os.path
import time
import datetime
import sys

import socket
 

def upload():
    os.system("git config --global user.email \"chyfemail163@163.com\"")
    os.system("git config --global user.name \"chyfemail163@163.com\"")
    os.system("git add .")
    os.system("git commit -m \"ui\"")
    # os.system("git commit")  
    os.system("git push -u origin master")

   
 
# 主函数的实现
if __name__ == "__main__":

    upload() 

    print "git_upload end"
