# pdf_converter

Momentan kann eine docx-datei in eine pdf-datei umgewandelt werden. Allerdings wird das Format nicht eingehalten.

## Ausführung

1. Projekt klonen und dann starten
2. Zum Hochladen einer DOCX-Datei und Konvertierung in PDF verwende folgenden API-Endpunkt. 

```http
POST http://localhost:8080/api/upload
```
Dazu einfach Postman verwenden und unter "form-data" die docx hinzugefügen mit dem key "file". 

3. Zum downloaden der konvertierten PDF-Datei verwende folgenden API-Endpunkt, wobei der Name der konvertierten Datei gleich bleibt (FileName.docx wird zu FileName.pdf):

```http
POST http://localhost:8080/api/pdf/{filename}
```
