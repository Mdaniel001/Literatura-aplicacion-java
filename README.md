#📚 Literatura Aplicación Java"

Literatura-Aplicación-Java es una aventura tecnológica desarrollada en Java, donde los libros, autores e idiomas cobran vida a través del código. Este proyecto, un reto desafiante de Alura Latam y Oracle, combina la potencia de Spring Boot y PostgreSQL para ofrecer una experiencia fluida y funcional.

✨ Funcionalidades

Explora las maravillas literarias con estas herramientas:

🔍 Busca libros por título: Encuentra tu obra favorita al instante.

📚 Lista libros registrados: Navega por todo nuestro catálogo de libros.

👨‍🎨 Lista autores registrados: Descubre quiénes están detrás de las historias.

⏰ Listar autores en un determinado año: Viaja en el tiempo y conoce autores por año.

🌐 Listar libros por idioma: Sumérgete en las culturas a través de los idiomas.

🚀 Tecnologías utilizadas

Este viaje literario está propulsado por:

Java: El motor principal del proyecto.

Spring Boot: Nuestra guía para una arquitectura poderosa y simple.

PostgreSQL: La bóveda donde almacenamos el conocimiento.

🔧 Requisitos previos

¡Prepárate antes de comenzar!

Java 17 o superior.

Maven 3.8.1 o superior.

PostgreSQL 12 o superior.

Un IDE, como IntelliJ IDEA o Eclipse.

🛠️ Instalación

Sigue estos pasos y échate a navegar por los mares del código:

Clona el repositorio:

git clone https://github.com/Mdaniel001/Literatura-aplicacion-java.git

Accede al directorio:

cd literatura-aplicacion-java

Configura la base de datos PostgreSQL:

Crea una base de datos llamada literatura.

Actualiza el archivo application.properties con tus credenciales:

spring.datasource.url=jdbc:postgresql://localhost:5432/literatura
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update

Construye el proyecto:

mvn clean install

Ejecuta la aplicación:

mvn spring-boot:run

🕹️ Uso

Conecta con la aplicación utilizando herramientas como Postman o curl. Por ejemplo, para buscar libros por título:

GET /api/libros?titulo={titulo}

¡Y explora todas las posibilidades que ofrece esta aplicación!

🤝 Contribución

¡Haz que este proyecto sea aún mejor! Sigue estos pasos:

Haz un fork del repositorio.

Crea una rama para tu contribución:

git checkout -b nueva-funcionalidad

Realiza tus cambios y haz un commit:

git commit -m "Agrega nueva funcionalidad"

Sube los cambios:

git push origin nueva-funcionalidad

Crea un Pull Request en el repositorio original.

🎨 Autor

Proyecto creado con pasión por Melkis Daniel Laguna Gomez, en el marco del reto de Alura Latam y Oracle. ¡Gracias por visitar este repositorio!
