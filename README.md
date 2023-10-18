# TALLER 6 -AREP


1. El servicio MongoDB es una instancia de MongoDB corriendo en una máquina virtual de EC2

2. LogService es un servicio REST que recibe una cadena, la almacena en la base de datos y responde en un objeto JSON con las 10 ultimas cadenas almacenadas en la base de datos y la fecha en que fueron almacenadas.

3. La aplicación web APP-LB-RoundRobin está compuesta por un cliente web y al menos un servicio REST. El cliente web tiene un campo y un botón y cada vez que el usuario envía un mensaje, este se lo envía al servicio REST y actualiza la pantalla con la información que este le regresa en formato JSON. El servicio REST recibe la cadena e implementa un algoritmo de balanceo de cargas de Round Robin, delegando el procesamiento del mensaje y el retorno de la respuesta a cada una de las tres instancias del servicio LogService.

## Arquitectura

![image](https://github.com/Andres7498/AREP-LAB06/blob/main/images/1.png)

## Solución

Para implementar la arquitectura lo primero que debemos hacer es crear las 5 instancias de maquinas virtuales. 
Cada una será un servidor.

Recomandación: Utilize el mismo grupo de seguridad para todas las instancias, con el fin de facilitar la configuración de los puertos mas adelante.

![image]()


### 1. Conectar a local

Debemos instalar java en las instancias de logService y el servidor de Round robin.
Para esto debemos conectarlos a la consola de las instancias, para esto usamos la coneccion ssh que nos ofrece aws.

![image]()

Abrimos una terminal de linux y ejecutamos el comando, teniendo en cuenta que en el mismo directorio debe estar la clave .pm de la instancia.

![image]()


### 2. Instalamos Java (versión 17)

Para instalar java 17 utilizamos el siguiente comando en la terminal de la instancia.

Recuerde que debe instalar java para las instancias de logService y la instancia de RoundRobin

```
sudo yum install java-17-amazon-corretto-devel
```

Luego de instalar java verificamos que se instalo conrrectamente:

```
java -version
```
### 3. Mongo

Para empezar implementaremos el servidor de mongo db.

Para esto abrimos una consola de la instancia de mongo localmente y por medio de vi creamos el siguiente archivo:

```
vi /etc/yum.repos.d/mongodb-org-6.0.repo
```

le agregamos la siguiente información:

```
[mongodb-org-6.0]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/amazon/2/mongodb-org/6.0/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-6.0.asc
```

Instalamos mongo en la instancia con el siguiente comando:

```
sudo yum install -y mongodb-org
```

Ahora debemos decirle a mongo que acepte cualquier ip para consultar la basen de datos.

Entramos al archivo mongod.conf con el siguiente comando:

```
vi /etc/mongod.conf
```

Y cambios la seccion bindIP.

![image]()

Por ultimo ejecutamos el servicio mongo con el siguiente comando:

```
sudo systemctl start mongod
```

Es importante recordar los siguientes comandos:

```
sudo systemctl status mongod  \\Conocer estado mongo
sudo systemctl stop mongod    \\Detener mongo
```

Verificamos su estado:

![image]()

En el codigo debemos cambiar la url de la conexion a mongo con la ip publica de la instancia de mongo seguido del puerto donde corre mongo (27017)

![image]()


### 4. Implementación Round Robin

Antes de utilizar las terminales de las instancias debemos modificar el codigo del servidor RoundRobin.

Para esto el round robin decidira sobre las 3 ip publicas de las intancias de los logServices

![image]()

El url completo para conectarnos a uns intancia de logService es:

```
http://{ip-logService}:4567/service

4567 es el puerto donde corren los logService, y /service el path de los métodos.
```

Una vez tenemos el codigo debemos subirlo a las instancias de LogService y RoundRobin.

Para esto debemos usar el protocolo sftp seguido del mismo comando que usamos para conectarnos a la consola localmente.
Para subirlo comprimimos la carpeta en un archivo .zip y utulizamos put.

![image]()

Nos salimos con el comando "exit" y entramos nuevamente a la consola con ssh

Para terminar descomprimimos la carpeta target.zip con el comando unzip:

```
unzip target.zip
```


### 5. Abrir los Puertos

Ahora debemos abrir los puertos de las instancias.

Para esto editamos las reglas de entrada del grupo de seguridad y agregamos los puertos.

![image]()


### 6. Ejecutar

Ejecutamos la carpeta target con el siguiente comando:

```
//Para las instancias de logService
java -cp "target/classes:target/dependency/*" org.example.LogServiceApp
```

![image]()

```
//Para la instancia de Round Robin
java -cp "target/classes:target/dependency/*" org.example.RoundRobin
```

![image]()

Para terminar abrimos el siguiente link en el navegador

```
//Estructura
http://{dns-publico-instancia-roundRobin}:{puerto-roundRobin}/

//Usado
http://ec2-54-226-142-6.compute-1.amazonaws.com:4566/
```

![image]()


### 7. Pruebas

Pagina mostrada

![image]()

Utilizamos el método post

![image]()

Utilizamos el método get

![image]()
