import pygame as py
from tkinter import Tk, filedialog
from tkinter import Tk, filedialog
import os

py.init()
py.font.init()

class PixelGrid:
    def __init__(self, width, height):
        self.width = width
        self.height = height
        self.grid = [[None for _ in range(width)] for _ in range(height)]  # None represents transparent

    def setPixel(self, x, y, color):
        if 0 <= x < self.width and 0 <= y < self.height:
            self.grid[y][x] = color

    def getPixel(self, x, y):
        if 0 <= x < self.width and 0 <= y < self.height:
            return self.grid[y][x]
        return None

    def clear(self):
        self.grid = [[None for _ in range(self.width)] for _ in range(self.height)]

    def save_sprite(self):
        root = Tk()
        root.withdraw()
        # Set default directory to the current working directory or a specific path
        default_dir = os.path.join(os.path.dirname(__file__), "src/sprites")  # Uses the directory of the current script
        file_path = filedialog.asksaveasfilename(
            initialdir=default_dir,
            defaultextension=".spr",
            filetypes=[("Sprite files", "*.spr"), ("All files", "*.*")]
        )
        root.destroy()
            
        if file_path:
            with open(file_path, "w") as file:
                # Write dimensions
                file.write(f"{self.width},{self.height}\n")
                
                # Write pixel data (None for transparent pixels)
                for row in self.grid:
                    row_data = []
                    for pixel in row:
                        if pixel is None:
                            row_data.append("(null)")  # Handle transparent pixels
                        else:
                            # RGB format
                            r, g, b = pixel
                            row_data.append(f"({r},{g},{b})")
                    file.write(":".join(row_data) + "\n")
                
    def load_sprite(self):
        root = Tk()
        root.withdraw()
        file_path = filedialog.askopenfilename(
            filetypes=[("Sprite files", "*.spr"), ("All files", "*.*")]
        )
        root.destroy()
        
        if file_path:
            with open(file_path, "r") as file:
                # Read dimensions
                dimensions = file.readline().strip().split(',')
                width, height = int(dimensions[0]), int(dimensions[1])
                
                # Initialize a new grid with the loaded dimensions
                self.width = width
                self.height = height
                self.grid = [[None for _ in range(width)] for _ in range(height)]
                            
                # Read pixel data
                for y in range(height):
                    row_data = file.readline()[0:-1].split(":")
                    for x, data in enumerate(row_data):
                        if data == "(null)":
                            self.setPixel(x, y, None)
                        else:
                            # Parse RGB values
                            data = data[1:-1].split(",")
                            r, g, b = int(data[0]), int(data[1]), int(data[2])
                            self.setPixel(x, y, (r, g, b))

class DisplayScreen:
    def __init__(self, width, height):
        self.width = width
        self.height = height

        self.screen = py.display.set_mode((self.width + 100, self.height))

    def getScreen(self):
        return self.screen

    def display(self, grid):
        # Draw pixels (background pattern)
        for y in range(grid.height):
            for x in range(grid.width):
                if x % 2 != y % 2:
                    py.draw.rect(self.screen, (40,40,40), (x * scale, y * scale, scale, scale))
                else:
                    py.draw.rect(self.screen, (30,30,30), (x * scale, y * scale, scale, scale))

                color = grid.getPixel(x, y)
                if color is not None:  # Only draw non-transparent pixels
                    py.draw.rect(self.screen, color, (x * scale, y * scale, scale, scale))
                elif color is None:  # Handle transparent pixels (None value)
                    # You could optionally add a visual indicator for transparent pixels
                    # For example, a small X or pattern to show it's transparent
                    pass

        # Draw grid lines
        for i in range(grid.width + 1):
            py.draw.line(self.screen, (70, 70, 70), (i * scale, 0), (i * scale, grid.height * scale))
        for i in range(grid.height + 1):
            py.draw.line(self.screen, (70, 70, 70), (0, i * scale), (grid.width * scale, i * scale))
 
        Sidebar.draw(self.screen, self.width, 0)

        py.display.update()

    def update(self, grid):
        self.display(grid)
        for event in py.event.get():
            if event.type == py.MOUSEBUTTONDOWN:
                if event.button == 1:  # Left mouse button pressed down
                    mouse_x, mouse_y = py.mouse.get_pos()
                    # Check for clicks on sidebar elements
                            
                    if mouse_x < self.width and mouse_y < self.height:
                        grid_x = mouse_x // scale
                        grid_y = mouse_y // scale
                        if Sidebar.clickedColorPicker:
                            # Handle color picking from the grid
                            pixel_color = grid.getPixel(grid_x, grid_y)
                            if pixel_color is not None:
                                Sidebar.setCurrentColor(pixel_color)
                                Sidebar.clickedColorPicker = False
                        else:
                            if Sidebar.eraser:
                                color = None
                            else:
                                color = Sidebar.getCurrentColor()
                            grid.setPixel(grid_x, grid_y, color)

            # Handle mouse drag (mouse motion while button is pressed)
            elif event.type == py.MOUSEMOTION:
                if py.mouse.get_pressed()[0]:  # Left mouse button is being held down
                    mouse_x, mouse_y = py.mouse.get_pos()
                    if mouse_x < self.width and mouse_y < self.height:
                        grid_x = mouse_x // scale
                        grid_y = mouse_y // scale
                        if Sidebar.eraser:
                            color = None
                        else:
                            color = Sidebar.getCurrentColor()
                        grid.setPixel(grid_x, grid_y, color)
            
            if event.type == py.QUIT:
                return False

        return True
    
    def run(self, grid):
        while True:
            if not self.update(grid):
                py.quit()
                break

class Sidebar:
    def __init__(self, width, height):
        self.width = width
        self.height = height
        self.eraser = False
        self.clickedColorPicker = False
        self.current_color = (255, 0, 0)
        self.color_obj = py.Color(*self.current_color) 

    def draw(self, screen, x, y):
        # Initialize UI constants
        self.padding = 10
        self.paddedWidth = self.width - 2 * self.padding
        self.grid_size = min(self.paddedWidth, 200)
        self.slider_height = 20
        self.text_height = 12
        self.text_padding = 3
        self.font = py.font.SysFont('Arial', 12)
        
        # Draw sidebar background
        screen.fill((150, 150, 150), (x, y, self.width, self.height))
        py.draw.rect(screen, (0, 0, 0), (x, y, self.width, self.height), 1)
        
        # Convert current color to pygame Color object
        self.color_obj = py.Color(*self.current_color)
        
        current_y = y + self.padding
        
        # Draw components
        current_y = self._draw_section(screen, x, current_y, "Current Color", self._draw_color_preview)
        current_y = self._draw_section(screen, x, current_y, "Color Picker", self._draw_color_grid)
        current_y = self._draw_section(screen, x, current_y, "Hue", self._draw_hue_slider)
        
        # Draw RGB/HSV values
        rgb_text = f"RGB: {self.current_color[0]}, {self.current_color[1]}, {self.current_color[2]}"
        hsv_text = f"HSV: {int(self.color_obj.hsva[0])}, {int(self.color_obj.hsva[1])}%, {int(self.color_obj.hsva[2])}%"
        
        rgb_label = self.font.render(rgb_text, True, (0, 0, 0))
        hsv_label = self.font.render(hsv_text, True, (0, 0, 0))
        
        screen.blit(rgb_label, (x + self.padding, current_y))
        screen.blit(hsv_label, (x + self.padding, current_y + self.text_height + self.text_padding))
        current_y += self.text_height * 2 + self.text_padding * 2 + self.padding

        # Add color picker button
        current_y = self._draw_button(screen, x, current_y, "Color Picker", (255, 255, 255) if self.clickedColorPicker else (220, 220, 220), self.pick_color)

        current_y = self._draw_button(screen, x, current_y, "Clear", (220, 220, 220), grid.clear)

        # Draw eraser button
        current_y = self._draw_button(screen, x, current_y, "Eraser", (255, 255, 255) if self.eraser else (220, 220, 220), self._toggle_eraser)
        
        # Draw save/load buttons
        current_y = self._draw_button(screen, x, current_y, "Save", (220, 220, 220), grid.save_sprite)
        current_y = self._draw_button(screen, x, current_y, "Load", (220, 220, 220), grid.load_sprite)

    def _draw_section(self, screen, x, y, title, draw_func):
        """Draw a section with a title and content"""
        # Draw section title
        label = self.font.render(title, True, (0, 0, 0))
        screen.blit(label, (x + self.padding, y))
        y += self.text_height + self.text_padding
        
        # Draw section content
        y = draw_func(screen, x, y)
        
        return y + self.padding

    def _draw_button(self, screen, x, y, text, color, click_handler):
        """Draw a button and handle clicks"""
        button_height = 30
        button_rect = py.draw.rect(screen, color, 
                               (x + self.padding, y, self.paddedWidth, button_height))
        py.draw.rect(screen, (0, 0, 0), button_rect, 1)
        
        text_surf = self.font.render(text, True, (0, 0, 0))
        screen.blit(text_surf, (x + self.padding + 5, y + button_height//2 - self.text_height//2))
        
        # Handle button clicks
        if py.mouse.get_pressed()[0]:
            mouse_pos = py.mouse.get_pos()
            button_id = f"{text.lower()}_clicked"
            if button_rect.collidepoint(mouse_pos) and not hasattr(self, button_id):
                setattr(self, button_id, True)
                click_handler()
        elif not py.mouse.get_pressed()[0]:
            button_id = f"{text.lower()}_clicked"
            if hasattr(self, button_id):
                delattr(self, button_id)
                
        return y + button_height + 10

    def _toggle_eraser(self):
        """Toggle eraser state"""
        self.eraser = not self.eraser
        self.clickedColorPicker = False
    
    def pick_color(self):
        self.clickedColorPicker = not self.clickedColorPicker
        self.eraser = False

    def _draw_color_grid(self, screen, x, y):
        # Get current HSV values
        current_hue = self.color_obj.hsva[0]
        current_sat = self.color_obj.hsva[1]
        current_val = self.color_obj.hsva[2]
        
        # Draw saturation-brightness grid
        for i in range(self.grid_size):
            for j in range(self.grid_size):
                saturation = 100 * i / self.grid_size
                brightness = 100 * (self.grid_size - j) / self.grid_size
                
                color = py.Color(0)
                color.hsva = (current_hue, saturation, brightness, 100)
                
                py.draw.rect(screen, color, (x + self.padding + i, y + j, 1, 1))
        
        # Draw grid border
        py.draw.rect(screen, (0, 0, 0), (x + self.padding - 1, y - 1, self.grid_size + 2, self.grid_size + 2), 1)
        
        # Draw SB indicator
        indicator_x = int(current_sat / 100 * self.grid_size)
        indicator_y = int((100 - current_val) / 100 * self.grid_size)
        py.draw.circle(screen, (255, 255, 255), (x + self.padding + indicator_x, y + indicator_y), 4, 1)
        
        # Handle grid clicks
        if py.mouse.get_pressed()[0]:
            mouse_pos = py.mouse.get_pos()
            if (x + self.padding <= mouse_pos[0] < x + self.padding + self.grid_size and 
                y <= mouse_pos[1] < y + self.grid_size):
                sat = 100 * (mouse_pos[0] - x - self.padding) / self.grid_size
                val = 100 * (self.grid_size - (mouse_pos[1] - y)) / self.grid_size
                color = py.Color(0)
                color.hsva = (current_hue, sat, val, 100)
                self.current_color = (color.r, color.g, color.b)
                self.eraser = False
                self.clickedColorPicker = False

        
        return y + self.grid_size

    def _draw_hue_slider(self, screen, x, y):
        current_hue = self.color_obj.hsva[0]
        
        # Draw hue slider
        for i in range(self.paddedWidth):
            hue_color = py.Color(0)
            hue_color.hsva = (int(i / self.paddedWidth * 360), 100, 100, 100)
            py.draw.rect(screen, hue_color, (x + self.padding + i, y, 1, self.slider_height))

        # Draw hue slider border
        py.draw.rect(screen, (0, 0, 0), (x + self.padding - 1, y - 1, self.paddedWidth + 2, self.slider_height + 2), 1)
        
        # Draw hue indicator
        indicator_pos = int(current_hue / 360 * self.paddedWidth)
        py.draw.rect(screen, (0, 0, 0), (x + self.padding + indicator_pos - 2, y - 3, 4, self.slider_height + 6), 2)
        
        # Handle hue slider clicks
        if py.mouse.get_pressed()[0]:
            mouse_pos = py.mouse.get_pos()
            if (x + self.padding <= mouse_pos[0] <= x + self.padding + self.paddedWidth and y <= mouse_pos[1] <= y + self.slider_height):
                hue = (mouse_pos[0] - x - self.padding) / self.paddedWidth * 360
                color = py.Color(0)
                current_sat = self.color_obj.hsva[1]
                current_val = self.color_obj.hsva[2]
                color.hsva = (hue, current_sat, current_val, 100)
                self.current_color = (color.r, color.g, color.b)
                self.eraser = False
                self.clickedColorPicker = False
        
        return y + self.slider_height

    def _draw_color_preview(self, screen, x, y):
        # Display current color preview
        preview_height = self.slider_height
        py.draw.rect(screen, self.current_color, (x + self.padding, y, self.paddedWidth, preview_height))
        
        # Draw border
        py.draw.rect(screen, (0, 0, 0), (x + self.padding - 1, y - 1, self.paddedWidth + 2, preview_height + 2), 1)
        
        return y + preview_height

    def getCurrentColor(self):
        return self.current_color

    def setCurrentColor(self, color):
        self.current_color = color
        self.eraser = False
        self.clickedColorPicker = False

if __name__ == "__main__":
    size = 12
    scale = 480 // size
    grid = PixelGrid(size, size)
    screen = DisplayScreen(size * scale, size * scale)
    Sidebar = Sidebar(100, 600)
    screen.run(grid)
