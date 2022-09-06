## Anforderungskatalog

1. Server-Client-Anwendung, die als Netzwerkmonitoring-Tool dient.
2. Die Server-Anwendung ist in Java geschrieben und läuft auf einem Windows-System.
3. Der Server führt die Netzwerkanalyse durch und stellt Endpunkte für die Webanwendung zur Verfügung.
4. Die Webanwendung stellt die Benutzeroberfläche zur Verfügung.
5. Die Benutzerauthentifizierung liegt im Tätigkeitsbereich der Webanwendung.
6. Für die Webanwendung werden Laravel und Bootstrap verwendet.
7. Über die Benutzeroberfläche kann der Anwender Parameter des zu analysierenden Netzwerkbereiches angeben. Diese werden
   an den Server gesendet, der das Netzwerk analysiert und der Webanwendung die Daten via JSON zurücksendet.
8. Zur Analyse des Netzwerkes wird das Address Resolution Protocol verwendet.
9. Die Anwendung kann um weitere Funktionalitäten erweitert werden.