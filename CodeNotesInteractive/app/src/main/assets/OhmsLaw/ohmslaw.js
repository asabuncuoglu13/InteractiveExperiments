// Ohmsches Gesetz
// Java-Applet (23.11.1997) umgewandelt
// 11.06.2016 - 23.06.2016

// ****************************************************************************
// * Autor: Walter Fendt (www.walter-fendt.de)                                *
// * Dieses Programm darf - auch in ver�nderter Form - f�r nicht-kommerzielle *
// * Zwecke verwendet und weitergegeben werden, solange dieser Hinweis nicht  *
// * entfernt wird.                                                           *
// ****************************************************************************

// Sprachabh�ngige Texte sind einer eigenen Datei (zum Beispiel ohmslaw_de.js) abgespeichert.

// Farben:

var colorBackground = "#F5F5F5";                           // Hintergrundfarbe
var colorVoltage = "#0000ff";                              // Farbe f�r Spannung
var colorAmperage = "#ff0000";                             // Farbe f�r Stromst�rke

// Sonstige Konstanten:

var FONT1 = "normal normal bold 14px sans-serif";          // Zeichensatz
var MAX_U = [1000, 300, 100, 30, 10, 3, 1];                // Messbereiche Spannung (V)
var MAX_I = [10, 3, 1, 0.3, 0.1, 0.03, 0.01, 0.003, 0.001];// Messbereiche Stromst�rke (A)

// Attribute:

var canvas, ctx;                                           // Zeichenfl�che, Grafikkontext
var width, height;                                         // Abmessungen der Zeichenfl�che (Pixel)
var selU, selI;                                            // Auswahlfelder
var buR1, buR2, buU1, buU2;                                // Schaltkn�pfe
var opU, opI;                                              // Ausgabefelder

var nrVoltage;                                             // Messbereichsnummer Spannung
var nrAmperage;                                            // Messbereichsnummer Stromst�rke
var nrResistance;                                          // Nummer Widerstandswert
var voltage;                                               // Spannung (V)
var maxVoltage;                                            // Messbereich Spannung (V)
var stepVoltage;                                           // Schrittweite Spannung (V)
var amperage;                                              // Stromst�rke (A)
var maxAmperage;                                           // Messbereich Stromst�rke (A)
var resistance;                                            // Widerstand (Ohm)

// Element der Schaltfl�che (aus HTML-Datei):
// id ..... ID im HTML-Befehl
// text ... Text (optional)

function getElement (id, text) {
  var e = document.getElementById(id);                     // Element
  if (text) e.innerHTML = text;                            // Text festlegen, falls definiert
  return e;                                                // R�ckgabewert
  } 

// Start:

function start () {
  canvas = getElement("cv");                               // Zeichenfl�che
  width = canvas.width; height = canvas.height;            // Abmessungen (Pixel)
  ctx = canvas.getContext("2d");                           // Grafikkontext  
  getElement("lbU",text01);                                // Erkl�render Text (Maximale Spannung)
  selU = getElement("selU");                               // Auswahlfeld (Maximale Spannung)
  prepareSelect(selU,MAX_U,volt,4);                        // Auswahlfeld vorbereiten
  getElement("lbI",text02);                                // Erkl�render Text (Maximale Stromst�rke)
  selI = getElement("selI");                               // Auswahlfeld (Maximale Stromst�rke)
  prepareSelect(selI,MAX_I,ampere,4);                      // Auswahlfeld vorbereiten
  buR1 = getElement("buR1",text03);                        // Schaltknopf (Widerstand vergr��ern)
  buR2 = getElement("buR2",text04);                        // Schaltknopf (Widerstand verkleinern)
  buU1 = getElement("buU1",text05);                        // Schaltknopf (Spannung vergr��ern)
  buU2 = getElement("buU2",text06);                        // Schaltknopf (Spannung verkleinern)
  opU = getElement("opU");                                 // Ausgabefeld (Spannung)
  opI = getElement("opI");                                 // Ausgabefeld (Stromst�rke)
  getElement("author",author);                             // Autor
  getElement("translator",translator);                     // �bersetzer
  
  nrVoltage = 4;                                           // Index f�r Messbereich Spannung (10 V) 
  nrAmperage = 4;                                          // Index f�r Messbereich Stromst�rke (100 mA)
  nrResistance = 19;                                       // Index f�r Widerstand
  voltage = 6;                                             // Spannung (V)
  amperage = 0.03;                                         // Stromst�rke (A)
  resistance = 200;                                        // Widerstand (Ohm)
  maxVoltage = 10;                                         // Messbereich Spannung (10 V)
  stepVoltage = 1;                                         // Schrittweite Spannung (1 V) 
  maxAmperage = 0.1;                                       // Messbereich Stromst�rke (100 mA)
 
  actionEnd();                                             // Hilfsroutine aufrufen (u.a. Ausgabe)
  paint();                                                 // Neu zeichnen
  selU.onchange = reactionChoiceU;                         // Reaktion auf Auswahlfeld (Messbereich Spannung)
  selI.onchange = reactionChoiceI;                         // Reaktion auf Auswahlfeld (Messbereich Stromst�rke)
  buR1.onclick = reactionButtonR1;                         // Reaktion auf Schaltknopf (Widerstand vergr��ern)
  buR2.onclick = reactionButtonR2;                         // Reaktion auf Schaltknopf (Widerstand verkleinern)
  buU1.onclick = reactionButtonU1;                         // Reaktion auf Schaltknopf (Spannung vergr��ern)
  buU2.onclick = reactionButtonU2;                         // Reaktion auf Schaltknopf (Spannung verkleinern)
  }
    
// Reaktion auf Auswahlfeld (Maximale Spannung):
// Seiteneffekt nrVoltage, maxVoltage, stepVoltage, amperage, Aktivierung/Deaktivierung von Schaltkn�pfen, Ausgabe 
  
function reactionChoiceU () {
  nrVoltage = selU.selectedIndex;                          // Index im Auswahlfeld
  maxVoltage = Math.pow(10,3-Math.floor(nrVoltage/2));     // Vorl�ufige Maximalspannung (Zehnerpotenz) 
  stepVoltage = maxVoltage/10;                             // Vorl�ufige Schrittweite
  if (nrVoltage%2 > 0) {                                   // Falls Index ungerade ...
    maxVoltage *= 0.3;                                     // Maximalspannung verkleinern 
    stepVoltage /= 2;                                      // Schrittweite verkleinern
    }   
  actionEnd();                                             // Hilfsroutine aufrufen
  }
  
// Reaktion auf Auswahlfeld (Maximale Stromst�rke):
// Seiteneffekt nrAmperage, maxAmperage, amperage, Aktivierung/Deaktivierung von Schaltkn�pfen, Ausgabe  
  
function reactionChoiceI () {
  nrAmperage = selI.selectedIndex;                         // Index im Auswahlfeld
  maxAmperage = Math.pow(10,1-Math.floor(nrAmperage/2));   // Vorl�ufige Maximalstromst�rke (Zehnerpotenz)
  if (nrAmperage%2 > 0) maxAmperage *= 0.3;                // Falls Index ungerade, Maximalstromst�rke verkleinern
  actionEnd();                                             // Hilfsroutine aufrufen
  }
  
// Reaktion auf ersten Schaltknopf (Widerstand vergr��ern):
// Seiteneffekt nrResistance, resistance, amperage, Aktivierung/Deaktivierung von Schaltkn�pfen, Ausgabe 
  
function reactionButtonR1 () {
  nrResistance++;                                          // Index f�r Widerstand erh�hen 
  resistance = (nrResistance%9+1)*Math.pow(10,Math.floor(nrResistance/9));     // Widerstand (Ohm)
  actionEnd();                                             // Hilfsroutine aufrufen
  }
  
// Reaktion auf zweiten Schaltknopf (Widerstand verkleinern):
// Seiteneffekt nrResistance, resistance, amperage, Aktivierung/Deaktivierung von Schaltkn�pfen, Ausgabe 
  
function reactionButtonR2 () {
  nrResistance--;                                          // Index f�r Widerstand erniedrigen
  resistance = (nrResistance%9+1)*Math.pow(10,Math.floor(nrResistance/9));     // Widerstand (Ohm)
  actionEnd();                                             // Hilfsroutine aufrufen
  }
  
// Reaktion auf dritten Schaltknopf (Spannung erh�hen):
// Seiteneffekt voltage, amperage, Aktivierung/Deaktivierung von Schaltkn�pfen, Ausgabe 
  
function reactionButtonU1 () {
  voltage = Math.min(voltage+stepVoltage,1000);            // Spannung erh�hen (Obergrenze 1000 V)
  voltage = Math.floor(voltage/stepVoltage+0.01)*stepVoltage; // Vielfaches der Schrittweite erzwingen 
  actionEnd();                                             // Hilfsroutine aufrufen
  }
  
// Reaktion auf vierten Schaltknopf (Spannung erniedrigen):
// Seiteneffekt voltage, amperage, Aktivierung/Deaktivierung von Schaltkn�pfen, Ausgabe 
  
function reactionButtonU2 () {
  voltage = Math.max(voltage-stepVoltage,0);               // Spannung erniedrigen (Untergrenze 0 V)
  voltage = Math.floor(voltage/stepVoltage+0.01)*stepVoltage; // Vielfaches der Schrittweite erzwingen
  actionEnd();                                             // Hilfsroutine aufrufen
  }
   
//-------------------------------------------------------------------------------------------------

// Auswahlfeld vorbereiten:
// ch ...... Auswahlfeld
// max ..... Array der Zahlenwerte
// unit .... Einheit
// index ... Gew�nschter Index (optional, Defaultwert 0)
  
function prepareSelect (ch, max, unit, index) {
  for (var i=0; i<max.length; i++) {                       // F�r alle Indizes ...
    var o = document.createElement("option");              // Neues option-Element
    var m = max[i], u = unit;                              // Zahlenwert und Einheit
    if (m < 1) {m *= 1000; u = symbolMilli+u;}             // Gegebenenfalls Verwendung von "Milli"
    o.text = String(m)+" "+u;                              // Zeichenkette 
    ch.add(o);                                             // option-Element hinzuf�gen
    }
  ch.selectedIndex = (index ? index : 0);                  // Index festlegen
  }

  
// Umwandlung einer Zahl in eine Zeichenkette:
// n ..... Gegebene Zahl
// d ..... Zahl der Stellen
// fix ... Flag f�r Nachkommastellen (im Gegensatz zu g�ltigen Ziffern)

function ToString (n, d, fix) {
  var s = (fix ? n.toFixed(d) : n.toPrecision(d));         // Zeichenkette mit Dezimalpunkt
  if (n == 1000 && d == 3 && !fix) s = "1000";             // 1.00e+3 verhindern
  return s.replace(".",decimalSeparator);                  // Eventuell Punkt durch Komma ersetzen
  }
  
// Zeichenkette f�r Gr��enangabe:
// symbol ... Symbol der Gr��e
// value .... Zahlenwert
// unit ..... Einheit
  
function value (symbol, value, unit) {
  return symbol+" = "+ToString(value,3,false)+" "+unit;    // R�ckgabewert
  }
  
// Aktualisierung der Ausgabe:
  
function updateOutput () {
  opU.innerHTML = value(symbolVoltage,voltage,volt);       // Ausgabe Spannung
  opI.innerHTML = value(symbolAmperage,amperage,ampere);   // Ausgabe Stromst�rke
  }
  
// Hilfsroutine: Berechnung der Stromst�rke, Aktivierung/Deaktivierung der Schaltkn�pfe, neu zeichnen, Ausgabe
  
function actionEnd () {
  amperage = voltage/resistance;                           // Stromst�rke (A)
  buR1.disabled = (nrResistance >= 45);                    // Schaltknopf f�r Vergr��erung des Widerstands
  buR2.disabled = (nrResistance <= 0)                      // Schaltknopf f�r Verkleinerung des Widerstands
  buU1.disabled = (voltage >= 1000);                       // Schaltknopf f�r Vergr��erung der Spannung
  buU2.disabled = (voltage <= 0);                          // Schaltknopf f�r Verkleinerung der Spannung
  paint();                                                 // Neu zeichnen
  updateOutput();                                          // Ausgabe aktualisieren
  }
   
//------------------------------------------------------------------------------------------------- 

// Neuer Grafikpfad (Standardwerte):

function newPath () {
  ctx.beginPath();                                         // Neuer Pfad
  ctx.strokeStyle = "#000000";                             // Linienfarbe schwarz
  ctx.lineWidth = 1;                                       // Liniendicke 1
  }
  
// Linie zeichnen (schwarz):
// x1, y1 ... Anfangspunkt
// x2, y2 ... Endpunkt
// d ........ Liniendicke (optional, Defaultwert 1)

function line (x1, y1, x2, y2, d) {
  newPath();                                               // Neuer Grafikpfad (Standardwerte)
  if (d) ctx.lineWidth = d;                                // Liniendicke festlegen, falls angegeben
  ctx.moveTo(x1,y1); ctx.lineTo(x2,y2);                    // Linie vorbereiten
  ctx.stroke();                                            // Linie zeichnen
  }
  
// Rechteck:
// (x,y) ... Koordinaten der Ecke links oben (Pixel)
// w ....... Breite (Pixel)
// h ....... H�he (Pixel)
// c ....... F�llfarbe (optional)

function rectangle (x, y, w, h, c) {
  if (c) ctx.fillStyle = c;                                // F�llfarbe
  newPath();                                               // Neuer Pfad
  if (c) ctx.fillRect(x,y,w,h);                            // Rechteck ausf�llen, falls F�llfarbe definiert
  ctx.strokeRect(x,y,w,h);                                 // Rand zeichnen
  }
  
// Kreisscheibe mit schwarzem Rand zeichnen:
// (x,y) ... Mittelpunktskoordinaten (Pixel)
// r ....... Radius (Pixel)
// c ....... F�llfarbe (optional)

function circle (x, y, r, c) {
  if (c) ctx.fillStyle = c;                                // F�llfarbe
  newPath();                                               // Neuer Grafikpfad (Standardwerte)
  ctx.arc(x,y,r,0,2*Math.PI,true);                         // Kreis vorbereiten
  ctx.fill();                                              // Kreis ausf�llen
  ctx.stroke();                                            // Rand zeichnen
  }
  
// Widerstand:

function resistor () {
  rectangle(225,185,80,30);                                // Rechteck als Widerstandssymbol
  ctx.textAlign = "center";                                // Zentrierte Textausgabe
  var s = ToString(resistance,0,true)+" "+ohm;             // Zeichenkette f�r Wert des Widerstands
  ctx.fillText(s,265,204);                                 // Beschriftung
  }
  
// Knoten:
// (x,y) ... Position (Pixel)
  
function node (x, y) {
  circle(x,y,2.5,"#000000");
  }
  
// Dr�hte:
    
function wires () {
  newPath();                                               // Neuer Grafikpfad (Standardwerte)
  ctx.moveTo(190,300);                                     // Anfangspunkt (linke Buchse Netzger�t) 
  ctx.lineTo(50,300);                                      // Weiter nach links
  ctx.lineTo(50,100);                                      // Weiter nach oben
  ctx.lineTo(185,100);                                     // Weiter nach rechts (linke Buchse Voltmeter)
  ctx.moveTo(215,100);                                     // Neuer Anfangspunkt (rechte Buchse Voltmeter)
  ctx.lineTo(350,100);                                     // Weiter nach rechts
  ctx.lineTo(350,300);                                     // Weiter nach unten
  ctx.lineTo(210,300);                                     // Weiter nach links (rechte Buchse Netzger�t)
  ctx.moveTo(50,200);                                      // Neuer Anfangspunkt (linker Knoten)
  ctx.lineTo(110,200);                                     // Weiter nach rechts (linke Buchse Amperemeter)
  ctx.moveTo(140,200);                                     // Neuer Anfangspunkt (rechte Buchse Amperemeter)
  ctx.lineTo(225,200);                                     // Weiter nach rechts (linkes Ende Widerstand)
  ctx.moveTo(305,200);                                     // Neuer Anfangspunkt (rechtes Ende Widerstand)
  ctx.lineTo(350,200);                                     // Weiter nach rechts (rechter Knoten)  
  ctx.stroke();                                            // Linien zeichnen
  node(190,300);                                           // Linke Buchse Netzger�t
  node(210,300);                                           // Rechte Buchse Netzger�t
  node(50,200);                                            // Linker Knoten                                          
  node(350,200);                                           // Rechter Knoten
  }
  
// Winkel f�r Messger�tskala (Bogenma�):
// part ... Teil des Vollausschlags

function angle (part) {
  if (part > 1) part = 1;                                  // Zu gro�en Ausschlag verhindern
  return (-1+2*part)*0.2*Math.PI;                          // R�ckgabewert
  }
  
// Striche f�r Skala eines Messger�ts:
// (x,y) .... Bezugspunkt (Pixel)
// n ........ Zahl der Teilintervalle
// r1, r2 ... Radien f�r Endpunkte (Pixel)

function ticks (x, y, n, r1, r2) {
  for (var i=0; i<=n; i++) {                               // F�r alle Indizes ...
    var w = angle(i/n);                                    // Winkel (Bogenma�)
    var sin = Math.sin(w), cos = Math.cos(w);              // Trigonometrische Werte
    var x0 = x+r1*sin, y0 = y-r1*cos;                      // Unteres Ende
    var x1 = x+r2*sin, y1 = y-r2*cos;                      // Oberes Ende
    line(x0,y0,x1,y1,2);                                   // Linie zeichnen    
    }
  }
  
// Messger�t allgemein:
// (x,y) .... Bezugspunkt
// type ..... Typ (0 Spannung, 1 Stromst�rke)
// n1, n2 ... Zahl der Intervalle
// part ..... Teil des Vollausschlags

function meter (x, y, type, n1, n2, part) {
  var w = angle(part);                                     // Winkel f�r Zeiger (Bogenma�)
  var x1 = x+35*Math.sin(w), y1 = y-35*Math.cos(w);        // Endpunkt Zeiger (Pixel)
  rectangle(x-30,y-50,60,40,"#ffffff");                    // Oberer Teil (wei�)
  line(x,y,x1,y1,2);                                       // Zeiger
  ticks(x,y,n1,38,45);                                     // L�ngere Striche der Skala  
  ticks(x,y,n2,40,45);                                     // K�rzere Striche dazwischen
  var  c = (type==0 ? colorVoltage : colorAmperage);       // Farbe f�r unteren Teil
  rectangle(x-30,y-10,60,20,c);                            // Unterer Teil (farbig) 
  node(x-15,y);                                            // Linke Buchse  
  node(x+15,y);                                            // Rechte Buchse
  ctx.fillStyle = "#000000";                               // Schriftfarbe
  ctx.textAlign = "center";                                // Textausrichtung
  ctx.fillText(type==0 ? volt : ampere,x,y+4);             // Einheit (Volt oder Ampere)
  if (part > 1) {                                          // Falls Messbereich �berschritten ...
    ctx.fillStyle = "#ff0000";                             // Schriftfarbe
    ctx.textAlign = "left";                                // Textausrichtung
    ctx.fillText(text07,x-70,y+30);                        // Warnmeldung (Messbereich �berschritten)
    }
  }
  
// Messger�t f�r Spannung:
      
function meterVoltage () {
  var t = (nrVoltage%2 == 0);                              // Typ (Zehnerpotenz oder dreifache Zehnerpotenz)
  var n1 = (t?2:3), n2 = (t?10:6);                         // Zahl der Intervalle
  meter(200,100,0,n1,n2,voltage/maxVoltage);               // Messger�t zeichnen
  }
  
// Messger�t f�r Stromst�rke:
  
function meterAmperage () {
  var t = (nrAmperage%2 == 0);                             // Typ (Zehnerpotenz oder dreifache Zehnerpotenz)
  var n1 = (t?2:3), n2 = (t?10:6);                         // Zahl der Intervalle
  meter(125,200,1,n1,n2,amperage/maxAmperage);             // Messger�t zeichnen
  }
  
// Zeichenfl�che aktualisieren:

function paint () {
  ctx.fillStyle = colorBackground;                         // Hintergrundfarbe
  ctx.fillRect(0,0,width,height);                          // Hintergrund ausf�llen
  ctx.font = FONT1;                                        // Zeichensatz
  rectangle(170,250,60,60,colorVoltage);                   // Geh�use Netzger�t
  circle(200,278,8,"#000000");                             // Drehknopf Netzger�t
  meterVoltage();                                          // Messger�t f�r Spannung
  meterAmperage();                                         // Messger�t f�r Stromst�rke
  wires();                                                 // Dr�hte
  resistor();                                              // Widerstand
  }
  
document.addEventListener("DOMContentLoaded",start,false); // Nach dem Laden der Seite Start-Methode aufrufen



