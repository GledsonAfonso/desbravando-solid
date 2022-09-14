# Desbravando SOLID

Markdown ebooks converter to formats like EPUB and PDF.

## Requirements:

- Java 17+
- Maven 3.8+

## How to use the CLI

To generate an `.zip` file with the necessary .jars and scripts, run:

```
mvn clean package
```

The `.zip` file should be in the `target` folder after this. Unzip the file to have access to the script with all the necessary dependencies to run the project.

### Generating an PDF file

To generate an PDF file, run:

```
./cotuba.sh -d directory/of/the/book -f pdf
```

An `book.pdf` should be generated after this.

### Generating an EPUB file

To generate an EPUB file, run:

```
./cotuba.sh -d directory/of/the/book -f epub
```

An `book.pdf` should be generated after this.

