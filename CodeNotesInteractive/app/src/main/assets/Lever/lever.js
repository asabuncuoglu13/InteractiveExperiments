// Hebelgesetz
// Java-Applet (02.11.1997), umgewandelt in HTML5/Javascript
// 02.04.2016 - 05.04.2016

// ****************************************************************************
// * Autor: Walter Fendt (www.walter-fendt.de)                                *
// * Dieses Programm darf - auch in ver�nderter Form - f�r nicht-kommerzielle *
// * Zwecke verwendet und weitergegeben werden, solange dieser Hinweis nicht  *
// * entfernt wird.                                                           *
// **************************************************************************** 

// Sprachabh�ngige Texte sind einer eigenen Datei (zum Beispiel lever_de.js) abgespeichert.

// Farben:

var colorBackground = "#F5F5F5";                           // Hintergrundfarbe
var colorLever1 = "#00ff00";                               // Farbe f�r Hebel (1)
var colorLever2 = "#ffc040";                               // Farbe f�r Hebel (2)
var colorLever3 = "#c0c0c0";                               // Farbe f�r Stativ
var	colorLeft = "#ff0000";                                 // Farbe f�r linksseitige Drehmomente
var colorRight = "#0000ff";                                // Farbe f�r rechtsseitige Drehmomente

// Sonstige Konstanten:

var FONT = "normal normal bold 12px sans-serif";           // Zeichensatz
var N = 11;                                                // Zahl der Felder pro Hebelarm
var DX = 20;                                               // Gr��e eines Felds (Pixel)
var DY = 6;                                                // Halbe Hebelbreite (Pixel)

// Attribute:

var canvas, ctx;                                           // Zeichenfl�che, Grafikkontext
var width, height;                                         // Abmessungen der Zeichenfl�che (Pixel)
var mx, my;                                                // Position Drehachse (Pixel)
var left, right;                                           // Arrays f�r Zahl der Massenst�cke (links/rechts)
var pgLever;                                               // Array f�r Polygonecken (ganzer Hebel)
var pgField;                                               // Doppelt indiziertes Array f�r Polygonecken (einzelne Felder)
var leftTorque, rightTorque;                               // Links- und rechtsseitiges Drehmoment
var cos, sin;                                              // Trigonometrische Werte f�r Drehmatrix
var hole;                                                  // Array f�r Koordinaten der L�cher
var drag;                                                  // Flag f�r Zugmodus
var mouseX, mouseY;                                        // Position Mauszeiger (Pixel)

// Start:
	
function start () {
  canvas = document.getElementById("cv");                  // Zeichenfl�che
  width = canvas.width; height = canvas.height;            // Abmessungen (Pixel)
  ctx = canvas.getContext("2d");                           // Grafikkontext
  mx = width/2; my = 80;                                   // Position Drehachse (Pixel)
  left = new Array(N); right = new Array(N);               // Arrays f�r Zahl der Massenst�cke links/rechts
  pgLever = new Array(4);;                                 // Array f�r Polygonecken (ganzer Hebel)
  pgField = new Array(N);                                  // Arrays f�r Polygonecken (einzelne Felder)
  for (var i=0; i<N; i++)                                  // F�r alle Indizes ...
    pgField[i] = new Array(4);                             // Array f�r Ecken eines einzelnen Feldes
  for (var i=0; i<N; i++)                                  // F�r alle Indizes ...
    left[i] = right[i] = 0;                                // Zun�chst keine Massenst�cke
  left[3] = 4; right[6] = 2;                               // Anfangssituation
  hole = new Array(2*N-1);                                 // Array f�r Koordinaten der L�cher
  drag = false;                                            // Flag f�r Zugmodus
  calculation();                                           // Berechnungen
  paint();                                                 // Zeichnen
  
  canvas.onmousedown = reactionMouseDown;                  // Reaktion auf Dr�cken der Maustaste
  canvas.ontouchstart = reactionTouchStart;                // Reaktion auf Ber�hrung  
  canvas.onmouseup = reactionMouseUp;                      // Reaktion auf Loslassen der Maustaste
  canvas.ontouchend = reactionTouchEnd;                    // Reaktion auf Ende der Ber�hrung
  canvas.onmousemove = reactionMouseMove;                  // Reaktion auf Bewegen der Maus      
  canvas.ontouchmove = reactionTouchMove;                  // Reaktion auf Bewegen des Fingers   
  }
  
// Reaktion auf Dr�cken der Maustaste:
  
function reactionMouseDown (e) {        
  reactionDown(e.clientX,e.clientY);                       // Hilfsroutine aufrufen                    
  }
  
// Reaktion auf Ber�hrung:
  
function reactionTouchStart (e) {      
  var obj = e.changedTouches[0];                           // Liste der Ber�hrpunkte, erster Punkt
  reactionDown(obj.clientX,obj.clientY);                   // Hilfsroutine aufrufen
  if (drag) e.preventDefault();                            // Falls Zugmodus aktiviert, Standardverhalten verhindern
  }
  
// Reaktion auf Loslassen der Maustaste:
  
function reactionMouseUp (e) {                                             
  drag = false;                                            // Zugmodus deaktivieren 
  reactionUp(e.clientX,e.clientY);                         // Hilfsroutine aufrufen                           
  }
  
// Reaktion auf Ende der Ber�hrung:
  
function reactionTouchEnd (e) {             
  drag = false;                                            // Zugmodus deaktivieren
  var obj = e.changedTouches[0];                           // Liste der Ber�hrpunkte, erster Punkt
  reactionUp(obj.clientX,obj.clientY);                     // Hilfsroutine aufrufen
  }
  
// Reaktion auf Bewegen der Maus:
  
function reactionMouseMove (e) { 
  if (!drag) return;                                       // Abbrechen, falls Zugmodus nicht aktiviert
  reactionMove(e.clientX,e.clientY);                       // Position ermitteln, rechnen und neu zeichnen   
  }
  
// Reaktion auf Bewegung des Fingers:
  
function reactionTouchMove (e) {   
  if (!drag) return;                                       // Abbrechen, falls Zugmodus nicht aktiviert
  var obj = e.changedTouches[0];                           // Liste der neuen Fingerpositionen, erster Punkt     
  reactionMove(obj.clientX,obj.clientY);                   // Hilfsroutine aufrufen
  e.preventDefault();                                      // Standardverhalten verhindern    
  }
  
// Hilfsroutine f�r reactionDown und reactionMove: Massenst�ck hinzuf�gen oder wegnehmen
// (u,v) ... Mausposition (Pixel)
// add ... +1 f�r Hinzuf�gen oder -1 f�r Wegnehmen
// Seiteneffekt left, right

function updateLeftRight (u, v, add) {
  if (add != 1 && add != -1) return;                       // Falls unzul�ssiger Wert, abbrechen
  var pos = number(u,v);                                   // Nummer eines Lochs
  if (pos >= 0 && pos < N-1) {                             // Falls linke Seite des Hebels ...
    var i = N-1-pos;                                       // Index im Array left 
    if (add == 1 && left[i] < 10) left[i]++;               // Gegebenenfalls Massenst�ck hinzuf�gen
    if (add == -1 && left[i] > 0) left[i]--;               // Gegebenenfalls Massenst�ck wegnehmen
    }
  if (pos > N-1 && pos <= 2*N-2) {                         // Falls rechte Seite des Hebels ...
    i = pos+1-N;                                           // Index im Array right
    if (add == 1 && right[i] < 10) right[i]++;             // Gegebenenfalls Massenst�ck hinzuf�gen
    if (add == -1 && right[i] > 0) right[i]--;             // Gegebenenfalls Massenst�ck wegnehmen
    }
  }
  
// Hilfsroutine: Reaktion auf Mausklick oder Ber�hren mit dem Finger:
// u, v ... Bildschirmkoordinaten bez�glich Viewport
// Seiteneffekt mouseX, mouseY, drag, left, right, leftTorque, rightTorque, cos, sin, pgLever, pgField, hole

function reactionDown (u, v) {
  var re = canvas.getBoundingClientRect();                 // Lage der Zeichenfl�che bez�glich Viewport
  u -= re.left; v -= re.top;                               // Koordinaten bez�glich Zeichenfl�che (Pixel) 
  mouseX = u; mouseY = v;                                  // Position Mauszeiger
  drag = true;                                             // Flag f�r Zugmodus
  updateLeftRight(u,v,-1);                                 // Gegebenenfalls Massenst�ck wegnehmen
  calculation();                                           // Berechnungen 
  paint();                                                 // Neu zeichnen
  }
  
// Reaktion auf Bewegung von Maus oder Finger:
// u, v ... Bildschirmkoordinaten bez�glich Viewport
// Seiteneffekt mouseX, mouseY, leftTorque, rightTorque, cos, sin, pgLever, pgField, hole

function reactionMove (u, v) {
  var re = canvas.getBoundingClientRect();                 // Lage der Zeichenfl�che bez�glich Viewport
  u -= re.left; v -= re.top;                               // Koordinaten bez�glich Zeichenfl�che (Pixel)
  mouseX = u; mouseY = v;                                  // Position Mauszeiger
  calculation();                                           // Berechnungen
  paint();                                                 // Neu zeichnen
  }
  
// Reaktion auf Loslassen der Maustaste oder Ende der Ber�hrung:
// u, v ... Bildschirmkoordinaten bez�glich Viewport
// Seiteneffekt mouseX, mouseY, left, right, drag, leftTorque, rightTorque, cos, sin, pgLever, pgField, hole
  
function reactionUp (u, v) {
  var re = canvas.getBoundingClientRect();                 // Lage der Zeichenfl�che bez�glich Viewport
  u -= re.left; v -= re.top;                               // Koordinaten bez�glich Zeichenfl�che (Pixel)  
  mouseX = u; mouseY = v;                                  // Position Mauszeiger
  updateLeftRight(u,v,1);                                  // Gegebenenfalls Massenst�ck hinzuf�gen
  drag = false;                                            // Flag f�r Zugmodus
  calculation();                                           // Berechnungen
  paint();                                                 // Neu zeichnen
  }
  
//-------------------------------------------------------------------------------------------------

// Umwandlung einer Zahl in eine Zeichenkette:
// n ..... Gegebene Zahl
// d ..... Zahl der Stellen
// fix ... Flag f�r Nachkommastellen (im Gegensatz zu g�ltigen Ziffern)

function ToString (n, d, fix) {
  var s = (fix ? n.toFixed(d) : n.toPrecision(d));         // Zeichenkette mit Dezimalpunkt
  return s.replace(".",decimalSeparator);                  // Eventuell Punkt durch Komma ersetzen
  }

// Drehung eines Punktes um die Drehachse:
// (x,y) ... urspr�nglicher Punkt (Koordinaten bez�glich Drehachse)
// Drehwinkel durch globale Variable sin und cos gegeben
// Ergebnisse in Fensterkoordinaten

function rotatePoint (x, y) {
  return {u: mx+cos*x-sin*y, v: my-sin*x-cos*y};
  }
  
// Festlegen einer Polygonecke:
// p ....... Array f�r Ecken des Polygons
// i ....... Index der Ecke
// (x,y) ... Koordinaten
  
function setVertex (p, i, x, y) {
  p[i] = rotatePoint(x,y);
  }
  
// Festlegen der Ecken eines (eventuell gedrehten) Rechtecks:

function setRectangle (pg, xL, xR) {
  setVertex(pg,0,xL,-DY);                                  // Ecke links unten 
  setVertex(pg,1,xR,-DY);                                  // Ecke rechts unten
  setVertex(pg,2,xR,DY);                                   // Ecke rechts oben
  setVertex(pg,3,xL,DY);                                   // Ecke links oben
  }

// Berechnungen:
// Seiteneffekt leftTorque, rightTorque, cos, sin, pgLever, pgField, hole

function calculation () {
  leftTorque = rightTorque = 0;                            // Startwert f�r links-/rechtsseitiges Drehmoment
  for (var i=1; i<N; i++) leftTorque += i*left[i];         // Linksseitiges Drehmoment
  for (i=1; i<N; i++) rightTorque += i*right[i];           // Rechtsseitiges Drehmoment
  if (leftTorque > rightTorque)  {cos = 0.96; sin = 0.28;} // Linksseitiges Drehmoment gr��er
  else if (leftTorque == rightTorque) {cos = 1; sin = 0;}  // Gleichgewicht
  else {cos = 0.96; sin = -0.28;}                          // Rechtsseitiges Drehmoment gr��er
  setRectangle(pgLever,-N*DX,N*DX);                        // Ecken des ganzen Hebels festlegen
  for (i=0; i<N; i++)                                      // F�r alle andersfarbigen Felder ...
    setRectangle(pgField[i],(1-N+2*i)*DX,(2-N+2*i)*DX);    // Ecken festlegen
  for (i=0; i<2*N-1; i++) {                                // F�r alle Indizes des Arrays hole ...
    var h = (i+1-N)*DX;                                    // Position relativ zum Mittelpunkt (ohne Drehung)
    hole[i] = rotatePoint(h,0);                            // Position des Lochs speichern
    }
  }
  
// Nummer einer Aufh�ngung (abh�ngig von Mausposition):
// (x,y) ... Mausposition (Pixel)
// R�ckgabewert: 0 bis N-2 f�r linke Seite, N bis 2N-2 f�r rechte Seite, bei Misserfolg -1

function number (x, y) {
  for (var i=0; i<2*N-1; i++) {                            // F�r alle Indizes des Arrays hole ...
    if (i == N-1) continue;                                // Index f�r Mittelpunkt �berspringen
    var dx = x-hole[i].u, dy = y-hole[i].v;                // Koordinatendifferenzen (Pixel)
    var n = 0;                                             // Startwert f�r Zahl der Massenst�cke
    if (i < N-1) n = left[N-1-i];                          // Zahl der Massenst�cke f�r linke Seite des Hebels
    if (i > N-1) n = right[i+1-N];                         // Zahl der Massenst�cke f�r rechte Seite des Hebels
    if (dx >= -5 && dx <= +5 && dy >= -5 && dy <= n*10+5)  // Falls Position im Bereich der h�ngenden Massenst�cke ... 
      return i;                                            // R�ckgabewert
    }
  return -1;                                               // R�ckgabewert bei Misserfolg
  }
  
//-------------------------------------------------------------------------------------------------

// Neuer Grafikpfad mit Standardwerten:

function newPath () {
  ctx.beginPath();                                         // Neuer Grafikpfad
  ctx.strokeStyle = "#000000";                             // Linienfarbe schwarz
  ctx.lineWidth = 1;                                       // Liniendicke 1
  }
  
// Linie:
// (x1,y1) ... Anfangspunkt (Koordinaten bez�glich Mittelpunkt)
// (x2,y2) ... Endpunkt (Koordinaten bez�glich Mittelpunkt)
// c ......... Farbe (optional)
// w ......... Liniendicke (optional, Defaultwert 1)

function line (x1, y1, x2, y2, c, w) {
  ctx.beginPath();                                         // Neuer Grafikpfad
  if (c) ctx.strokeStyle = c;                              // Linienfarbe, falls angegeben
  ctx.lineWidth = (w ? w : 1);                             // Liniendicke, falls angegeben
  ctx.moveTo(x1,y1); ctx.lineTo(x2,y2);                    // Linie vorbereiten
  ctx.stroke();                                            // Linie zeichnen
  }
  
// Achsenparalleles Rechteck mit schwarzem Rand:
// (x,y) ... Ecke links oben
// w ....... Breite
// h ....... H�he
// c ....... F�llfarbe

function rectangle (x, y, w, h, c) {
  newPath();                                               // Neuer Grafikpfad (Standardwerte)
  ctx.fillStyle = c;                                       // F�llfarbe
  ctx.fillRect(x,y,w,h);                                   // Rechteck ausf�llen
  ctx.strokeRect(x,y,w,h);                                 // Rechtecksrand (schwarz)
  }
  
// Ausgef�lltes Polygon mit schwarzem Rand:
// p ... Array der Ecken
// c ... F�llfarbe
     
function polygon (p, c) {
  newPath();                                               // Neuer Grafikpfad (Standardwerte) 
  ctx.moveTo(p[0].u,p[0].v);                               // Anfangspunkt
  for (var i=1; i<p.length; i++)                           // F�r alle weiteren Punkte ...
    ctx.lineTo(p[i].u,p[i].v);                             // Verbindungslinie zum Grafikpfad hinzuf�gen
  ctx.closePath();                                         // Zur�ck zum Anfangspunkt
  ctx.fillStyle = c;                                       // F�llfarbe 
  ctx.fill();                                              // Polygon ausf�llen
  ctx.stroke();                                            // Schwarzer Rand
  }
  
// Ausgef�llter schwarzer Kreis:
// pt ... Mittelpunkt (Koordinaten u, v, Pixel)
// r .... Radius (Pixel)

function circle (pt, r) {
  newPath();                                               // Neuer Grafikpfad (Standardwerte)
  ctx.arc(pt.u,pt.v,r,0,2*Math.PI,true);                   // Kreis vorbereiten
  ctx.fillStyle = "#000000";                               // F�llfarbe
  ctx.fill();                                              // Kreis ausf�llen
  }
  
// Einzelnes Massenst�ck:
// (x,y) ... Position der Aufh�ngung (Pixel)

function mass (x, y) {
  newPath();                                               // Neuer Grafikpfad (Standardwerte)
  line(x,y,x,y+5);                                         // Faden
  rectangle(x-4,y+5,8,5,"#000000");                        // Massenst�ck
  }
  
// Massenst�cke:
// dx ... Position der Aufh�ngung relativ zum Mittelpunkt (ohne Drehung, Pixel)
// n .... Anzahl
// c .... Farbe

function masses (dx, n, c) {
  if (n <= 0) return;                                      // Falls Anzahl nicht positiv, abbrechen
  newPath();                                               // Neuer Grafikpfad (Standardwerte)
  var pt = rotatePoint(dx,0);                              // Position der Aufh�ngung unter Ber�cksichtigung der Drehung
  var x = pt.u, y = pt.v;                                  // Koordinaten der Aufh�ngung
  line(x,y,x,y+5);                                         // Oberster Faden
  for (var i=0; i<n; i++) {                                // F�r alle Massenst�cke ...
    var yy = y+i*10+5;                                     // Senkrechte Koordinate
    line(x,yy,x,yy+5);                                     // Faden
    rectangle(x-4,yy+5,8,5,c);                             // Massenst�ck
    }
  } 
  
// Hebel mit Massenst�cken:

function lever () {
  rectangle(mx-5,my-20,10,140,colorLever3);                // Stativ
  line(mx-150,my+120,mx+150,my+120);                       // Boden
  polygon(pgLever,colorLever1);                            // Hebel insgesamt
  for (var i=0; i<N; i++) polygon(pgField[i],colorLever2); // Andersfarbige Felder
  for (i=1; i<N; i++) masses(-i*DX,left[i],colorLeft);     // Massenst�cke links   
  for (i=1; i<N; i++) masses(i*DX,right[i],colorRight);    // Massenst�cke rechts
  circle(0,0,2.5);                                         // Drehachse
  for (i=0; i<2*N-1; i++) circle(hole[i],2);               // L�cher zum Aufh�ngen von Massenst�cken 
  circle(hole[N-1],3);                                     // Drehachse 
  }
  
// Text mit Index:
// s ....... Zeichenkette ('_' als Trennzeichen zwischen normalem Text und Index)
// (x,y) ... Position (Pixel)
// R�ckgabewert: x-Koordinate zum Weiterschreiben (nach Text, Index und Leerzeichen)

function textIndex (s, x, y) {
  var i = s.indexOf("_");                                  // Index f�r Unterstrich
  var s1 = (i>=0 ? s.substring(0,i) : s);                  // Zeichenkette vor dem Unterstrich (normaler Text)
  var s2 = (i>=0 ? s.substring(i+1) : "");                 // Zeichenkette nach dem Unterstrich (Index)
  ctx.textAlign = "left";                                  // Textausrichtung
  ctx.fillText(s1,x,y);                                    // Normalen Text ausgeben
  x += ctx.measureText(s1).width;                          // x-Koordinate f�r Index
  if (i >= 0) ctx.fillText(s2,x,y+4);                      // Index ausgeben
  return x+ctx.measureText(s2+" ").width;                  // R�ckgabewert
  }
  
// Zeichenkette f�r Gr��enangabe:
// v ... Zahlenwert
// n ... Anzahl der Nachkommastellen
// u ... Einheit

function size (v, n, u) {
  return ToString(v,n,true)+" "+u;
  }
  
// Zeichenkette f�r Berechnung der Drehmomente der linken Seite:

function termLeft () {
  var s = "= ";                                            // Gleichheitszeichen und Leerzeichen
  var first = true;                                        // Flag f�r ersten Summanden
  for (var i=N; i>=1; i--) {                               // F�r alle Indizes (von links nach rechts) ...
    var f = left[i];                                       // Kraft (N)                         
    if (f > 0) {                                           // Falls Summand gr��er als 0 ...
      if (!first) s += " + ";                              // Falls n�tig, Pluszeichen hinzuf�gen
      s += size(f,1,newton)+" "+symbolMult+" "+size(i/10,2,meter); // Produkt (Kraft mal Hebelarm) hinzuf�gen
      first = false;                                       // Flag f�r ersten Summanden
      } 
    }
  return s+" =";                                           // R�ckgabewert                                         
  }
  
// Zeichenkette f�r Berechnung der Drehmomente der rechten Seite:

function termRight () {
  var s = "= ";                                            // Gleichheitszeichen und Leerzeichen
  var first = true;                                        // Flag f�r ersten Summanden
  for (var i=1; i<=N; i++) {                               // F�r alle Indizes (von links nach rechts) ...
    var f = right[i];                                      // Kraft (N)
    if (f > 0) {                                           // Falls Summand gr��er als 0 ...
      if (!first) s += " + ";                              // Falls n�tig, Pluszeichen hinzuf�gen
      s += size(f,1,newton)+" "+symbolMult+" "+size(i/10,2,meter); // Produkt (Kraft mal Hebelarm) hinzuf�gen
      first = false;                                       // Flag f�r ersten Summanden
      } 
    }
  return s+" =";                                           // R�ckgabewert
  }
  
// Ausgabe: Berechnung der Drehmomente
// nr ... 0 f�r linksseitig oder 1 f�r rechtsseitig

function output (nr) {
  if (nr != 0 && nr != 1) return;                          // Bei sinnlosem Wert von nr abbrechen
  ctx.fillStyle = (nr==0 ? colorLeft : colorRight);        // Schriftfarbe
  ctx.textAlign = "left";                                  // Textausrichtung
  var s = (nr==0 ? text01 : text02);                       // �berschrift
  var x = 20;                                              // Position waagrecht (Pixel)
  ctx.fillText(s,x,260+nr*70);                             // Erkl�render Text (links- oder rechtsseitiges Drehmoment)
  s = (nr==0 ? symbolTorqueLeft : symbolTorqueRight);      // Symbol f�r links- oder rechtsseitiges Drehmoment
  x = textIndex(s,x,280+nr*70);                            // Symbol ausgeben (im Allgemeinen mit Index)
  var s1 = (nr==0 ? termLeft() : termRight());             // Zeichenkette f�r Berechnungszeile
  var torque = (nr==0 ? leftTorque : rightTorque)/10;      // Betrag des Drehmoments (einseitig)
  var s2 = "= "+ToString(torque,1,true)+" "+newton+meter;  // Zeichenkette f�r Ergebniszeile    
  if (torque != 0 && ctx.measureText(s1).width < 500) {    // Falls Drehmoment ungleich 0 und Term nicht zu lang ...
    ctx.fillText(s1,x,280+nr*70);                          // Rechenausdruck f�r Drehmoment (einseitig)
    ctx.fillText(s2,x,300+nr*70);                          // Ergebnis
    }
  else ctx.fillText(s2,x,280+nr*70);                       // Andernfalls Ergebnis ohne Rechenausdruck
  }
  
// Zeichnen:
  
function paint () {
  ctx.fillStyle = colorBackground;                         // Hintergrundfarbe
  ctx.fillRect(0,0,width,height);                          // Hintergrund ausf�llen
  ctx.font = FONT;                                         // Zeichensatz
  lever();                                                 // Hebel mit Massenst�cken
  output(0);                                               // Ausgabe: Berechnung des linksdrehenden Drehmoments
  output(1);                                               // Ausgabe: Berechnung des rechtsdrehenden Drehmoments
  ctx.fillStyle = "#000000";                               // Schriftfarbe
  ctx.textAlign = "right";                                 // Textausrichtung
  if (drag) mass(mouseX,mouseY);                           // Bewegtes Massenst�ck 
  }

document.addEventListener("DOMContentLoaded",start,false); // Nach dem Laden der Seite Start-Methode aufrufen


