# pdf_converter

Der Fokus dieses Projektes war die Konvertierung von Dateien ins PDF.
Momentan können nur  docx-dateien in pdf-dateien umgewandelt werden und das Format wird nicht eingehalten.

## Ausführung

1. Projekt klonen und dann starten
2. Zum Hochladen einer DOCX-Datei und Konvertierung in PDF verwende folgenden API-Endpunkt. 

```http
POST http://localhost:8080/api/upload
```
Dazu einfach Postman verwenden und unter "form-data" die docx hinzugefügen mit dem key "file". 

Der Name der konvertierten Datei bleibt gleich (z.B. FileName.docx wird zu FileName.pdf)

3. Zum downloaden der konvertierten PDF-Datei verwende folgenden API-Endpunkt:

```http
POST http://localhost:8080/api/pdf/{filename}
```


### Randnotiz
Zunächst wurde Apache PDFBox verwendet, danach wurde aber zu Apache POI und iText umgestiegen. Beide Ansätze wurden zunächst mal im Code gelassen.

Die folgende Funktion verwendet Apache PDFBox zur PDF-Erstellung. Sie erstellt ein neues PDF-Dokument und fügt manuell Texte aus den DOCX-Absätzen hinzu.
```python
convertDocxToPDFWithApachePDFBox(file)
```
Die angepasste Funktion nutzt Apache POI für die DOCX-Verarbeitung und iText für die PDF-Erstellung. Sie liest die DOCX-Absätze direkt ein und fügt sie mit iText dem PDF-Dokument hinzu.
```python
convertDocxToPdfWithApachePOI(file)
```
