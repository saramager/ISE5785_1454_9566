# ISE5785_1454_9566
# 3D Ray Tracing Scene Renderer

This project was developed as part of the *Introduction to Software Engineering* course.  
It simulates and renders a **realistic 3D graphical scene** using ray tracing, incorporating many physical effects such as:
- Light sources and rays
- Reflection and refraction
- Color blending
- Shadows

## ✨ Advanced Features

In addition to the basic ray tracing engine, we implemented the following enhancements:

- **Multithreading** – Significantly improves performance by rendering using multiple threads in parallel.
  
- **Anti-Aliasing (Curve Smoothing)** – Reduces jagged edges caused by pixelization using multiple samples per camera ray.

- **Glass and Matte Mirror Materials** – Enhances realism by simulating diffuse reflections and semi-transparent glass, using multiple reflection and refraction rays per pixel.

- **Uniform Grid Acceleration (Voxel Grid)** – Partitions the scene into a 3D grid of uniform cells ("voxels"), and accelerates ray-object intersection using **3D DDA** (Digital Differential Analyzer) ray traversal algorithm.

These improvements contribute to both the **visual quality** and the **computational efficiency** of the rendering engine.

## 🚀 Technologies Used

- Java
- Object-Oriented Programming
- Multithreading
- Ray tracing engine 
- 3D DDA

## Authors
Sara Mager – github.com/saramager
Malka Haupt- github.com/malka00

All rights reserved © 2025 by the authors.




