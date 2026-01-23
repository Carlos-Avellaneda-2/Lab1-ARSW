# Lab1-ARSW
Desarrollo del lab 1 de arsw 2026-1

## Cambio en el incio con 'start()' ó 'run()'. Cómo cambia la salida?, por qué?.
Lo que sucede es que cuando se ejecutan los 3 hilos usando start() estos se ejecutan de manera simultanea haciendo que los 3 terminen a la vez debido a sus procesos duren lo mismo, mientras tanto el ejecutar los hilos con run() los ejecuta de manera escalada, esto quiere decir que primero se ejecutara el primer hilo (segun en el orden como se organizaron en el codigo) y hasta que no termine el primer hilo el siguiente no se ejecutara el siguiente.


