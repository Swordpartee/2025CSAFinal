import pygame as py
from tkinter import Tk, filedialog
from tkinter import Tk, filedialog
import os
import numpy as np
from PIL import Image

width = 0
height = 0
grid = [[]]

def setPixel(x, y, color):
    if 0 <= x < width and 0 <= y < height:
        grid[y][x] = color

def load_sprite(sprfilepath):
    global width, height, grid
    file_path = sprfilepath
    
    if file_path:
        with open(file_path, "r") as file:
            # Read dimensions
            dimensions = file.readline().strip().split(',')
            width, height = int(dimensions[0]), int(dimensions[1])
            
            # Initialize a new grid with the loaded dimensions
            width = width
            height = height
            grid = [[None for _ in range(width)] for _ in range(height)]
                        
            # Read pixel data
            for y in range(height):
                row_data = file.readline()[0:-1].split(":")
                for x, data in enumerate(row_data):
                    if data == "(null)":
                        setPixel(x, y, None)
                    else:
                        # Parse RGB values
                        data = data[1:-1].split(",")
                        r, g, b = int(data[0]), int(data[1]), int(data[2])
                        setPixel(x, y, (r, g, b))
                            
                            
def save_as_png(sprfilepath):
    load_sprite(sprfilepath)
    
    # Create the output file path in the pngResources folder with .png extension
    base_name = os.path.splitext(os.path.basename(sprfilepath))[0]
    file_path = os.path.join("pngResources", base_name + ".png")
    
    if file_path:
        # Create an array for the image (RGBA)
        img_array = np.zeros((height, width, 4), dtype=np.uint8)
        for y in range(height):
            for x in range(width):
                pixel = grid[y][x]
                if pixel is None:
                    img_array[y, x] = [0, 0, 0, 0]  # Transparent
                else:
                    r, g, b = pixel
                    img_array[y, x] = [r, g, b, 255]  # Opaque

        img = Image.fromarray(img_array, 'RGBA')
        img.save(file_path)
        

def process_all_spr_files(resources_folder="src/main/resources"):
    for root, dirs, files in os.walk(resources_folder):
        print(f"Processing directory: {root}")
        for file in files:
            if file.lower().endswith(".spr"):
                # print("A")
                spr_path = os.path.join(root, file)
                save_as_png(spr_path)
                
process_all_spr_files()