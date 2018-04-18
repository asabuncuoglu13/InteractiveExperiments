// Gew�hnlicher Flaschenzug mit gerader Rollenzahl
// Java-Applet (24.03.1998) umgewandelt
// 11.11.2014 - 10.09.2015

// ****************************************************************************
// * Autor: Walter Fendt (www.walter-fendt.de)                                *
// * Dieses Programm darf - auch in ver�nderter Form - f�r nicht-kommerzielle *
// * Zwecke verwendet und weitergegeben werden, solange dieser Hinweis nicht  *
// * entfernt wird.                                                           *
// **************************************************************************** 

// Sprachabh�ngige Texte sind einer eigenen Datei (zum Beispiel pulleysystem_de.js) abgespeichert.

// Farben:

var colorBackground = "#F5F5F5";                           // Hintergrundfarbe
var colorLoad = "#ffffff";                                 // Farbe f�r Last
var colorWeight = "#ff0000";                               // Farbe f�r Gewichtskraft
var colorForce = "#0000ff";                                // Farbe f�r ben�tigte Kraft
var colorSpringscale = "#4040ff";                          // Farbe f�r Geh�use der Federwaage
var colorSpringscale1 = "#ff0000";                         // Farbe f�r Skala der Federwaage
var colorSpringscale2 = "#ffffff";                         // Farbe f�r Skala der Federwaage
var colorPulleys = "#000000";                              // Farbe f�r Rollen
var colorPulleys1 = "#0000ff";                             // Farbe f�r feste Flasche
var colorPulleys2 = "#c00080";                             // Farbe f�r lose Flasche
var colorBase = "#ffa040";                                 // Farbe f�r Tisch

// Sonstige Konstanten:

var DEG = Math.PI/180;                                     // 1 Grad (Bogenma�)
var XR = 100;                                              // x-Koordinate der Rollenmittelpunkte
var YR1 = 15, YR2 = 35, YR3 = 51;                          // y-Koordinaten der Rollenmittelpunkte
var RR1 = 11, RR2 = 8, RR3 = 5;                            // Radien der Rollen (Pixel)
var HU = 60;                                               // H�he der Unterlage (Pixel)
var ARRMAX = 60;                                           // Maximale Pfeill�nge (Pixel)

// Attribute:

var canvas, ctx;                                           // Zeichenfl�che, Grafikkontext
var width, height;                                         // Abmessungen der Zeichenfl�che (Pixel)
var ch;                                                    // Auswahlfeld (Zahl der Rollen)
var ip1, ip2;                                              // Eingabefelder (Gewicht der Last bzw. der losen Flasche)
var op;                                                    // Ausgabefeld (Kraft)
var rb1, rb2;                                              // Radiobuttons (Federwaage, Kraftvektoren)
var active;                                                // Flag f�r Zugmodus
var nR;                                                    // Zahl der Rollen pro Flasche
var xB1, yB1;                                              // Ber�hrpunkt (oberste Rolle)
var lF;                                                    // L�nge einer Flasche (Pixel)
var polygon1;                                              // Polygon f�r Geh�use der Federwaage
var polygon2;                                              // Polygon f�r einzelnes Feld der Federwaagenskala
var xM, yM;                                                // Mausposition
var h;                                                     // H�he �ber der Unterlage (Pixel)
var fG;                                                    // Gewicht der Last (N)
var fGlF;                                                  // Gewicht der losen Flasche (N)
var f;                                                     // Ben�tigte Kraft (N)
var part;                                                  // Dehnung der Federwaage (Bruchteil)

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
  nR = 2;                                                  // Zun�chst 4 Rollen (2 Rollen pro Flasche)
  ch = getElement("ch");                                   // Auswahlliste (Zahl der Rollen)
  getElement("r2",text01);                                 // Eintrag (2 Rollen)
  getElement("r4",text02);                                 // Eintrag (4 Rollen)
  getElement("r6",text03);                                 // Eintrag (6 Rollen)
  getElement("ip1a",text04);                               // Erkl�render Text (Gewicht der Last)
  ip1 = getElement("ip1b");                                // Eingabefeld (Gewicht der Last)
  getElement("ip1c",newton);                               // Einheit (Gewicht der Last)
  getElement("ip2a",text05);                               // Erkl�render Text (Gewicht der losen Flasche)
  ip2 = getElement("ip2b");                                // Eingabefeld (Gewicht der losen Flasche)
  getElement("ip2c",newton);                               // Einheit (Gewicht der losen Flasche)
  getElement("opa",text06);                                // Erkl�render Text (ben�tigte Kraft)
  op = getElement("opb");                                  // Ausgabefeld (ben�tigte Kraft)
  rb1 = getElement("rb1");                                 // Radiobutton (Federwaage)
  getElement("lb1",text07);                                // Text f�r Radiobutton (Federwaage)
  rb2 = getElement("rb2");                                 // Radiobutton (Kraftvektoren)
  getElement("lb2",text08);                                // Text f�r Radiobutton (Kraftvektoren)
  rb1.checked = true;                                      // H�kchen f�r Federwaage
  getElement("author",author);                             // Autor
  getElement("translator",translator);                     // �bersetzer
  
  polygon1 = new Array(4);                                 // Polygon f�r Geh�use der Federwaage
  polygon2 = new Array(4);                                 // Polygon f�r Skala der Federwaage 
  nR = 2;                                                  // Startwert f�r Zahl der Rollen pro Flasche
  lF = 48;                                                 // Startwert f�r L�nge einer Flasche (Pixel)
  fG = 14;                                                 // Startwert f�r Gewicht der Last (N) 
  fGlF = 2;                                                // Startwert f�r Gewicht der losen Flasche (N)
  xM = width/2; yM = height/2;                             // Mausposition (Mittelpunkt der Zeichenfl�che)    
  active = false;                                          // Zugmodus abgeschaltet
  ch.selectedIndex = nR-1;                                 // Auswahlliste aktualisieren
  updateInput();                                           // Eingabefelder aktualisieren
  reaction();                                              // Rechnung, Ausgabe, neu zeichnen
  
  ch.onchange = reactionSelect;                            // Reaktion auf Auswahl (Zahl der Rollen)
  ip1.onkeydown = reactionEnter;                           // Reaktion auf Eingabe (Gewicht der Last)
  ip2.onkeydown = reactionEnter;                           // Reaktion auf Eingabe (Gewicht der losen Flasche)
  rb1.onclick = paint;                                     // Reaktion auf Radiobutton (Zeichnung mit Federwaage)
  rb2.onclick = paint;                                     // Reaktion auf Radiobutton (Zeichnung mit Kraftpfeilen)
  canvas.onmousedown = reactionMouseDown;                  // Reaktion auf Dr�cken der Maustaste
  canvas.ontouchstart = reactionTouchStart;                // Reaktion auf Ber�hrung  
  canvas.onmouseup = reactionMouseUp;                      // Reaktion auf Loslassen der Maustaste
  canvas.ontouchend = reactionTouchEnd;                    // Reaktion auf Ende der Ber�hrung
  canvas.onmousemove = reactionMouseMove;                  // Reaktion auf Bewegen der Maus      
  canvas.ontouchmove = reactionTouchMove;                  // Reaktion auf Bewegen des Fingers   
  } // Ende der Methode start
  
// Reaktion auf Auswahl in der Liste:
// Seiteneffekt nR, lF, fG, fGlF, f, xB1, yB1, h, part, xM, yM, Wirkung auf Ein- und Ausgabefelder

function reactionSelect () {
  nR = 1+ch.selectedIndex;                                 // Zahl der Rollen pro Flasche
  if (nR == 1) lF = 30;                                    // L�nge einer Flasche mit 1 Rolle 
  else if (nR == 2) lF = 48;                               // L�nge einer Flasche mit 2 Rollen 
  else if (nR == 3) lF = 60;                               // L�nge einer Flasche mit 3 Rollen
  updateInput();                                           // Aktualisierung der Eingabefelder
  reaction();                                              // Berechnungen, Ausgabe, neu zeichnen
  }
  
// Reaktion auf Eingabe mit Enter-Taste:
// Seiteneffekt fG, fGlF, f, xB1, yB1, h, part, xM, yM, Wirkung auf Ein- und Ausgabefelder
  
function reactionEnter (e) {
  if (e.key && String(e.key) == "Enter"                    // Falls Entertaste (Firefox/Internet Explorer) ...
  || e.keyCode == 13) {                                    // Falls Entertaste (Chrome) ...
    input();                                               // Eingabe
    reaction();                                            // Berechnungen, Ausgabe, neu zeichnen
    }                      
  }
   
// Reaktion auf Dr�cken der Maustaste:
  
function reactionMouseDown (e) {        
  reactionDown(e.clientX,e.clientY);                       // Eventuell Zugmodus aktivieren                    
  }
  
// Reaktion auf Ber�hrung:
  
function reactionTouchStart (e) {
  var obj = e.changedTouches[0];
  reactionDown(obj.clientX,obj.clientY);                   // Eventuell Zugmodus aktivieren      
  }
  
// Reaktion auf Loslassen der Maustaste:
// Seiteneffekt active
  
function reactionMouseUp (e) {                                             
  active = false;                                          // Zugmodus deaktivieren                             
  }
  
// Reaktion auf Ende der Ber�hrung:
// Seiteneffekt active
  
function reactionTouchEnd (e) {             
  active = false;                                          // Zugmodus deaktivieren
  }
  
// Reaktion auf Bewegen der Maus:
  
function reactionMouseMove (e) {            
  if (!active) return;                                     // Abbrechen, falls Zugmodus nicht aktiviert
  reactionMove(e.clientX,e.clientY);                       // Position ermitteln, rechnen und neu zeichnen
  }
  
// Reaktion auf Bewegung des Fingers:
  
function reactionTouchMove (e) {            
  if (!active) return;                                     // Abbrechen, falls Zugmodus nicht aktiviert
  var obj = e.changedTouches[0];                           // Liste der neuen Fingerpositionen     
  reactionMove(obj.clientX,obj.clientY);                   // Position ermitteln, rechnen und neu zeichnen
  e.preventDefault();                                      // Standardverhalten verhindern                          
  }  
  
// Reaktion auf Mausklick oder Ber�hrung:
// Seiteneffekt active, xB1, yB1, h, part, xM, yM

function reactionDown (x, y) {
  var re = canvas.getBoundingClientRect();                 // Lage der Zeichenfl�che bez�glich Viewport
  x -= re.left; y -= re.top;                               // Koordinaten bez�glich Zeichenfl�che
  if (Math.abs(x-xM) < 20 && Math.abs(y-yM) < 20)          // Falls Position beim Schnurende ... 
    active = true;                                         // ... Flag f�r Zugmodus setzen
  calcPosition(x,y);                                       // Berechnungen
  }
    
// Reaktion auf Bewegung von Maus oder Finger (�nderung):
// x, y ... Bildschirmkoordinaten bez�glich Viewport
// Seiteneffekt xB1, yB1, h, part, xM, yM, Wirkung auf Ein- und Ausgabefelder 

function reactionMove (x, y) {
  var re = canvas.getBoundingClientRect();                 // Lage der Zeichenfl�che bez�glich Viewport
  x -= re.left; y -= re.top;                               // Koordinaten bez�glich Zeichenfl�che (Pixel)
  calcPosition(x,y);                                       // Berechnungen
  paint();                                                 // Neu zeichnen
  }

//-------------------------------------------------------------------------------------------------

// Automatische Ab�nderung ungeeigneter Zahlenwerte, Berechnung der Kraft:
// Seiteneffekt fG, fGlF, f, Wirkung auf Ausgabefeld

function calcForce () {
  if (fG < 0) fG = 0;                                      // Negatives Gewicht der Last verhindern
  if (fGlF < 0) fGlF = 0;                                  // Negatives Gewicht der losen Flasche verhindern
  var maxG = 20*nR;                                        // Gewicht entsprechend Maximalbelastung der Federwaage (N)
  if (fG > maxG) fG = maxG;                                // Falls Gewicht der Last zu gro�, korrigieren
  if (fG+fGlF > maxG) fGlF = maxG-fG;                      // Falls Gewicht der losen Flasche zu gro�, korrigieren
  f = (fG+fGlF)/(2*nR);                                    // Zugkraft berechnen (N)
  updateOutput();                                          // Ausgabefeld aktualisieren 
  }
  
// Winkel des Ber�hrpunkts der Tangente durch einen gegebenen Punkt:
// (mx,my) ... Kreismittelpunkt
// r ......... Radius
// (x,y) ..... Gegebener Punkt
// R�ckgabewert: Winkel gegen�ber der x-Achse (Gegenuhrzeigersinn, Bogenma�)

function angle (mx, my, r, x, y) {
  var dx = x-mx, dy = my-y;                                // Verbindungsvektor 
  var c = Math.sqrt(dx*dx+dy*dy);                          // Betrag des Verbindungsvektors 
  return Math.atan2(dy,dx)+Math.acos(r/c);                 // R�ckgabewert
  }
    
// Korrektur der Mausposition, Berechnung von H�he und Ber�hrpunkt:
// (x,y) ... Mausposition
// Seiteneffekt xB1, yB1, h, part, xM, yM

function calcPosition (x, y) {
  if (x < XR+30) x = XR+30;                                // Falls Position zu weit links, korrigieren 
  if (x > 390) x = 390;                                    // Falls Position zu weit rechts, korrigieren
  if (y < 20) y = 20;                                      // Falls Position zu weit oben, korrigieren 
  if (y > height-10) y = height-10;                        // Falls Position zu weit unten, korrigieren
  var w = angle(XR,YR1,RR1,x,y);                           // Winkel f�r Ber�hrpunkt
  var cos = Math.cos(w), sin = Math.sin(w);                // Trigonometrische Werte
  xB1 = XR+RR1*cos; yB1 = 5+YR1-RR1*sin;                   // Ber�hrpunkt f�r obere Rolle             
  var dx = x-xB1, dy = y-yB1;                              // Verbindungsvektor Ber�hrpunkt/Mausposition 
  var d = Math.sqrt(dx*dx+dy*dy);                          // Betrag des Verbindungsvektors
  var arc = RR1*(Math.PI/2-w);                             // Kreisbogen f�r anliegende Schnur (Pixel)
  h = 0; part = 0;                                         // Startwerte: Last am Boden, Feder nicht gedehnt                                     
  var d1 = 85-arc;                                         // Erste kritische Entfernung zum Ber�hrpunkt
  var d2 = (rb1.checked ? d1+4*f : d1);                    // Zweite kritische Entfernung zum Ber�hrpunkt (Last am Boden)
  if (d < d1) {                                            // Falls Position zu nahe an der Rolle ...                                       
    x = xB1+d1*sin;                                        // ... x-Koordinate korrigieren
    y = yB1+d1*cos;                                        // ... y-Koordinate korrigieren
    }                        
  else if (d < d2)                                         // Falls Last am Boden ...
    part = (d-d1)/40;                                      // ... Bruchteil f�r Federwaage berechnen
  else {                                                   // Falls Normalfall (Last angehoben) ...
    h = (d-d2)/(2*nR);                                     // ... H�he �ber der Tischplatte berechnen 
    part = f/10;                                           // ... Bruchteil f�r Federwaage berechnen
    }
  xM = x; yM = y;                                          // Mausposition (eventuell korrigiert) speichern
  }
  
// Umwandlung einer Zahl in eine Zeichenkette:
// n ..... Gegebene Zahl
// d ..... Zahl der Stellen
// fix ... Flag f�r Nachkommastellen (im Gegensatz zu g�ltigen Ziffern)

function ToString (n, d, fix) {
  var s = (fix ? n.toFixed(d) : n.toPrecision(d));         // Zeichenkette mit Dezimalpunkt
  return s.replace(".",decimalSeparator);                  // Eventuell Punkt durch Komma ersetzen
  }
  
// Eingabe einer Zahl
// ef .... Eingabefeld
// d ..... Zahl der Nachkommastellen
// min ... Minimum des erlaubten Bereichs
// max ... Maximum des erlaubten Bereichs
// R�ckgabewert: Zahl
  
function inputNumber (ef, d, min, max) {
  var s = ef.value;                                        // Zeichenkette im Eingabefeld
  s = s.replace(decimalSeparator,".");                     // Eventuell Komma in Punkt umwandeln
  var n = Number(s);                                       // Umwandlung in Zahl, falls m�glich
  if (isNaN(n)) n = 0;                                     // Sinnlose Eingaben als 0 interpretieren 
  if (n < min) n = min;                                    // Falls Zahl zu klein, korrigieren
  if (n > max) n = max;                                    // Falls Zahl zu gro�, korrigieren
  ef.value = ToString(n,d,true);                           // Eingabe verwenden (eventuell korrigiert)
  return n;                                                // R�ckgabewert
  }
  
// Gesamte Eingabe:
// Seiteneffekt fG, fGlF, f, eventuell Korrektur in Eingabefeldern 

function input () {
  fG = inputNumber(ip1,1,0,nR*20);                         // Gewicht der Last (N)
  fGlF = inputNumber(ip2,1,0,nR*20);                       // Gewicht der losen Flasche (N)
  calcForce();                                             // Berechnung der Zugkraft, eventuell Korrektur der eingegebenen Werte
  updateInput();                                           // Eingabefelder aktualisieren                                           
  }
  
// Aktualisierung der Eingabefelder:

function updateInput () {
  ip1.value = ToString(fG,1,true);                         // Gewicht der Last (N)
  ip2.value = ToString(fGlF,1,true);                       // Gewicht der losen Flasche (N)
  }

// Aktualisierung des Ausgabefelds:

function updateOutput () {
  var sum = (fGlF != 0);                                   // Flag f�r Summe (und Klammer)
  var s1 = symbolForce + " = ";                            // Anfang (F = )
  var s2 = ToString(fG,1,true)+" "+newton;                 // Gewicht der Last
  if (sum) s2 += " + "+ToString(fGlF,1,true)+" "+newton;   // Summe der beiden Gewichte, falls n�tig
  if (sum) s2 = "("+s2+")";                                // Klammer um Summe, falls n�tig
  s2 += " "+symbolDivision+" "+Number(2*nR);               // Division durch Zahl der Rollen
  var s3 = " = "+ToString(f,2,true)+" "+newton;            // Ergebnis (Zugkraft)
  op.innerHTML = s1+s2+s3;                                 // Ausgabefeld aktualisieren
  }
  
// Reaktion: Rechnung, Ausgabe, neu zeichnen
// Seiteneffekt fG, fGlF, f, xB1, yB1, h, part, xM, yM, Wirkung auf Ausgabefeld

function reaction () {
  calcForce();                                             // Zugkraft berechnen
  calcPosition(xM,yM);                                     // Weitere Berechnungen
  updateOutput();                                          // Ausgabefelder aktualisieren
  paint();                                                 // Neu zeichnen
  } 
  
//-------------------------------------------------------------------------------------------------

// Neuer Grafikpfad mit Standardwerten:

function newPath () {
  ctx.beginPath();                                         // Neuer Pfad
  ctx.strokeStyle = "#000000";                             // Linienfarbe schwarz
  ctx.lineWidth = 1;                                       // Liniendicke 1
  }
  
// Linie der Dicke 1:
// (x1,y1) ... Anfangspunkt
// (x2,y2) ... Endpunkt
// c ......... Farbe (optional)
// w ......... Liniendicke (optional)

function line (x1, y1, x2, y2, w) {
  ctx.beginPath();                                         // Neuer Pfad
  ctx.lineWidth = (w ? w : 1);                             // Liniendicke, falls angegeben
  ctx.moveTo(x1,y1); ctx.lineTo(x2,y2);                    // Linie vorbereiten
  ctx.stroke();                                            // Linie zeichnen
  }
  
// Rechteck mit schwarzem Rand:
// (x,y) ... Koordinaten der Ecke links oben (Pixel)
// w ....... Breite (Pixel)
// h ....... H�he (Pixel)
// c ....... F�llfarbe (optional)

function rectangle (x, y, w, h, c) {
  if (c) ctx.fillStyle = c;                                // F�llfarbe
  newPath();                                               // Neuer Pfad
  ctx.fillRect(x,y,w,h);                                   // Rechteck ausf�llen
  ctx.strokeRect(x,y,w,h);                                 // Rand zeichnen
  }
  
// Kreisscheibe mit schwarzem Rand:
// (x,y) ... Mittelpunktskoordinaten (Pixel)
// r ....... Radius (Pixel)
// c ....... F�llfarbe (optional)

function circle (x, y, r, c) {
  if (c) ctx.fillStyle = c;                                // F�llfarbe
  newPath();                                               // Neuer Pfad
  ctx.arc(x,y,r,0,2*Math.PI,true);                         // Kreis vorbereiten
  ctx.fill();                                              // Kreis ausf�llen
  ctx.stroke();                                            // Rand zeichnen
  }
  
// Kreisbogen:
// (x,y) ... Mittelpunktskoordinaten (Pixel)
// r ....... Radius (Pixel)
// a0 ...... Startwinkel (Bogenma�)
// a ....... Winkelbetrag (Bogenma�) 
// w ....... Liniendicke (optional, Defaultwert 1)

function arc (x, y, r, a0, a, w) {
  newPath();                                               // Neuer Pfad (Standardwerte)
  ctx.lineWidth = (w ? w : 1);                             // Liniendicke
  ctx.arc(x,y,r,2*Math.PI-a0,2*Math.PI-a0-a,true);         // Kreisbogen vorbereiten
  ctx.stroke();                                            // Kreisbogen zeichnen
  }
    
// Pfeil zeichnen:
// x1, y1 ... Anfangspunkt
// x2, y2 ... Endpunkt
// w ........ Liniendicke (optional)
// Zu beachten: Die Farbe wird durch ctx.strokeStyle bestimmt.

function arrow (x1, y1, x2, y2, w) {
  if (!w) w = 1;                                           // Falls Liniendicke nicht definiert, Defaultwert                          
  var dx = x2-x1, dy = y2-y1;                              // Vektorkoordinaten
  var length = Math.sqrt(dx*dx+dy*dy);                     // L�nge
  if (length == 0) return;                                 // Abbruch, falls L�nge 0
  dx /= length; dy /= length;                              // Einheitsvektor
  var s = 2.5*w+7.5;                                       // L�nge der Pfeilspitze 
  var xSp = x2-s*dx, ySp = y2-s*dy;                        // Hilfspunkt f�r Pfeilspitze         
  var h = 0.5*w+3.5;                                       // Halbe Breite der Pfeilspitze
  var xSp1 = xSp-h*dy, ySp1 = ySp+h*dx;                    // Ecke der Pfeilspitze
  var xSp2 = xSp+h*dy, ySp2 = ySp-h*dx;                    // Ecke der Pfeilspitze
  xSp = x2-0.6*s*dx; ySp = y2-0.6*s*dy;                    // Einspringende Ecke der Pfeilspitze
  ctx.beginPath();                                         // Neuer Pfad
  ctx.lineWidth = w;                                       // Liniendicke
  ctx.moveTo(x1,y1);                                       // Anfangspunkt
  if (length < 5) ctx.lineTo(x2,y2);                       // Falls kurzer Pfeil, weiter zum Endpunkt, ...
  else ctx.lineTo(xSp,ySp);                                // ... sonst weiter zur einspringenden Ecke
  ctx.stroke();                                            // Linie zeichnen
  if (length < 5) return;                                  // Falls kurzer Pfeil, keine Spitze
  ctx.beginPath();                                         // Neuer Pfad f�r Pfeilspitze
  ctx.fillStyle = ctx.strokeStyle;                         // F�llfarbe wie Linienfarbe
  ctx.moveTo(xSp,ySp);                                     // Anfangspunkt (einspringende Ecke)
  ctx.lineTo(xSp1,ySp1);                                   // Weiter zum Punkt auf einer Seite
  ctx.lineTo(x2,y2);                                       // Weiter zur Spitze
  ctx.lineTo(xSp2,ySp2);                                   // Weiter zum Punkt auf der anderen Seite
  ctx.closePath();                                         // Zur�ck zum Anfangspunkt
  ctx.fill();                                              // Pfeilspitze zeichnen 
  }
  
// Polygon zeichnen:
// p ... Array mit Koordinaten der Ecken (u, v)
// c ... F�llfarbe

function drawPolygon (p, c) {
  ctx.beginPath();                                         // Neuer Grafikpfad
  ctx.strokeStyle = "#000000";                             // Linienfarbe schwarz
  ctx.fillStyle = c;                                       // F�llfarbe
  ctx.lineWidth = 1;                                       // Liniendicke
  ctx.moveTo(p[0].u,p[0].v);                               // Zur ersten Ecke
  for (var i=1; i<p.length; i++)                           // F�r alle weiteren Ecken ... 
    ctx.lineTo(p[i].u,p[i].v);                             // Linie zum Pfad hinzuf�gen
  ctx.closePath();                                         // Zur�ck zum Ausgangspunkt
  ctx.fill(); ctx.stroke();                                // Polygon ausf�llen und Rand zeichnen   
  }
  
// Feste Flasche:
// y ... oberes Ende (Pixel)

function pulleys1 (y) {
  circle(XR,y+YR1,RR1,colorPulleys);                       // Gr��te Rolle (oben)
  if (nR > 1) circle(XR,y+YR2,RR2,colorPulleys);           // Mittlere Rolle
  if (nR > 2) circle(XR,y+YR3,RR3,colorPulleys);           // Kleinste Rolle (unten)
  rectangle(XR-4,y,8,lF,colorPulleys1);                    // Flasche
  circle(XR,y+YR1,1,"#000000");                            // Obere Drehachse
  if (nR > 1) circle(XR,y+YR2,1,"#000000");                // Mittlere Drehachse
  if (nR > 2) circle(XR,y+YR3,1,"#000000");                // Untere Drehachse
  }

// Lose Flasche und Last:
// y ... unteres Ende

function pulleys2 (y) {
  circle(XR,y-YR1,RR1,colorPulleys);                       // Gr��te Rolle (unten)                    
  if (nR > 1) circle(XR,y-YR2,RR2,colorPulleys);           // Mittlere Rolle
  if (nR > 2) circle(XR,y-YR3,RR3,colorPulleys);           // Kleinste Rolle (oben)
  rectangle(XR-4,y-lF,8,lF,colorPulleys2);                 // Flasche
  line(XR,y,XR,y+5);                                       // Schnurst�ck (Aufh�ngung der Last)
  rectangle(XR-10,y+5,20,10,colorLoad);                    // Last
  circle(XR,y-YR1,1,"#000000");                            // Untere Drehachse
  if (nR > 1) circle(XR,y-YR2,1,"#000000");                // Mittlere Drehachse
  if (nR > 2) circle(XR,y-YR3,1,"#000000");                // Obere Drehachse
  }
  
// Schnur:

function string () {
  var yO = 5, yU = height-HU-10-h;                         // y-Koordinaten f�r oberes und unteres Ende (?)
  ctx.strokeStyle = "#000000";                             // Linienfarbe schwarz
  line(XR-RR1+1,yO+YR1,XR-RR1+1,yU-YR1);                   // Schnurst�ck links
  if (nR == 1) {line(XR+RR1-1,yU-YR1,XR,yO+lF); return;}   // Schnurende rechts f�r Spezialfall (2 Rollen)
  line(XR+RR1-1,yU-YR1,XR+RR2-1,yO+YR2);                   // Schnurst�ck rechts 
  line(XR-RR2+1,yO+YR2,XR-RR2+1,yU-YR2);                   // Schnurst�ck links (weiter innen)
  if (nR == 2) {line(XR+RR2-1,yU-YR2,XR,yO+lF); return;}   // Schnurende rechts innen f�r Spezialfall (4 Rollen)
  line(XR+RR2-1,yU-YR2,XR+RR3-1,yO+YR3);                   // Schnurst�ck rechts (weiter innen)
  line(XR-RR3+1,yO+YR3,XR-RR3+1,yU-YR3);                   // Schnurst�ck links (ganz innen)
  line(XR+RR3-1,yU-YR3,XR,yO+lF);                          // Schnurende rechts (ganz innen)
  }
   
// Hilfsroutine: Schr�g liegendes Rechteck vorbereiten
// p .......... Polygon (wird beeinflusst)
// (x,y) ...... Bezugspunkt (Mittelpunkt der Rechtecksseite rechts unten)
// v1x, v1y ... Vektor in L�ngsrichtung (nach links oben)
// v2x, v2y ... Vektor in Querrichtung (nach links unten)

function prepareRectangle (p, x, y, v1x, v1y, v2x, v2y) {
  p[0] = {u: x+v2x, v: y+v2y};                             // Ecke links unten 
  p[1] = {u: x+v1x+v2x, v: y+v1y+v2y};                     // Ecke links oben
  p[2] = {u: x+v1x-v2x, v: y+v1y-v2y};                     // Ecke rechts oben
  p[3] = {u: x-v2x, v: y-v2y};                             // Ecke rechts unten
  }
  
// Federwaage:
// (gx,gy) ... Mittelpunkt des Griffs
// w ........ Winkel gegen�ber der Waagrechten (Bogenma�)
// part ..... Bruchteil der maximal zul�ssigen Kraft
// Gesamtl�nge minimal 65 Pixel, maximal 105 Pixel
    
function springscale (gx, gy, w, part) {
  var r0 = 5;                                              // Radius f�r Griff (Pixel)
  var r1 = 5, a1 = 45;                                     // Radius und L�nge f�r Geh�use (Pixel)
  var r2 = 3, a2 = 4;                                      // Radius und L�nge f�r ein Feld der Skala (Pixel)
  circle(gx,gy,r0,colorSpringscale);                       // Griff au�en
  circle(gx,gy,r0-2,colorBackground);                      // Griff innen (Hintergrundfarbe) 
  var sin = Math.sin(w), cos = Math.cos(w);                // Trigonometrische Werte     
  var x = gx+r0*sin, y = gy+r0*cos;                        // Bezugspunkt f�r Geh�use (Mittelpunkt der Seite rechts unten)
  var v1x = a1*sin, v1y = a1*cos;                          // Vektor f�r Geh�use (L�ngsrichtung, nach links oben)
  var v2x = r1*cos, v2y = -r1*sin;                         // Vektor f�r Geh�use (Querrichtung, nach links unten)
  prepareRectangle(polygon1,x,y,v1x,v1y,v2x,v2y);          // Polygon f�r Geh�use vorbereiten
  var w1x = a2*sin, w1y = a2*cos;                          // Vektor f�r Skala (Feld, L�ngsrichtung, nach links oben)
  var w2x = r2*cos, w2y = -r2*sin;                         // Vektor f�r Skala (Feld, Querrichtung, nach links unten)
  var p10 = part*10;                                       // Hilfsgr��e    
  for (var i=Math.floor(10-p10); i<10; i++) {              // F�r alle sichtbaren Felder der Skala ...
    var x1 = x+(a1-10*a2)*sin+(p10+i)*w1x;                 // x-Koordinate des Bezugspunkts 
    var y1 = y+(a1-10*a2)*cos+(p10+i)*w1y;                 // y-Koordinate des Bezugspunkts
    prepareRectangle(polygon2,x1,y1,w1x,w1y,w2x,w2y);      // Polygon (Feld der Skala) vorbereiten
    var c = (i%2==0 ? colorSpringscale1 : colorSpringscale2); // Farbe des Felds        
    drawPolygon(polygon2,c);                               // Polygon (Feld der Skala) zeichnen
    }
  drawPolygon(polygon1,colorSpringscale);                  // Polygon (Geh�use) zeichnen
  var a3 = 4, r3 = 3;                                      // Abmessungen f�r Haken (Pixel)        
  var ax = x+(a1+p10*a2)*sin;                              // Anfangspunkt des Hakens, x-Koordinate 
  var ay = y+(a1+p10*a2)*cos;                              // Anfangspunkt des Hakens, y-Koordinate   
  var bx = ax+a3*sin, by = ay+a3*cos;                      // Endpunkt des geraden Teils des Hakens
  line(ax,ay,bx,by,1.5);                                   // Gerader Teil des Hakens
  var cx = bx+r3*sin, cy = by+r3*cos;                      // Mittelpunkt des gebogenen Teils des Hakens
  arc(cx,cy,r3,w+135*DEG,300*DEG,1.5);                     // Kreisbogen f�r gebogenen Teil des Hakens
  }
  
// Grafikausgabe:
  
function paint () {
  ctx.fillStyle = colorBackground;                         // Hintergrundfarbe
  ctx.fillRect(0,0,width,height);                          // Hintergrund ausf�llen
  rectangle(0,0,width,4,"#000000");                        // Decke
  rectangle(0,height-HU,XR+20,5,"#000000");                // Tischplatte
  rectangle(0,height-HU+5,XR-20,HU-5,colorBase);           // Tisch    
  pulleys1(5);                                             // Feste Flasche
  pulleys2(height-HU-15-Math.max(h,0));                    // Lose Flasche
  string();                                                // Schnur links  
  var alpha = Math.atan2(yB1-yM,xM-xB1);                   // Winkel f�r Ber�hrpunkt (Bogenma�)
  var cos = Math.cos(alpha), sin = Math.sin(alpha);        // Trigonometrische Werte
  if (rb1.checked) {                                       // 1. Fall (Federwaage)
    var d = 60+part*40;                                    // Hilfsgr��e 
    line(xM-d*cos,yM+d*sin,xB1,yB1);                       // Schnur rechts
    springscale(xM,yM,1.5*Math.PI+alpha,part);             // Federwaage
    }
  else {                                                   // 2. Fall (Kraftvektoren)
    line(xM,yM,xB1,yB1);                                   // Schnur rechts
    var max = Math.max(fG,f);                              // Maximum der durch Pfeile dargestellten Kr�fte (N)
    if (max == 0) return;                                  // Division durch 0 vermeiden
    ctx.strokeStyle = colorForce;                          // Farbe f�r Kraftpfeil
    if (f > 0) {                                           // Falls Zugkraft gr��er als 0 ...
      var lf = ARRMAX*f/max;                               // Pfeill�nge (Pixel)
      arrow(xM-lf*cos,yM+lf*sin,xM,yM,3);                  // Pfeil f�r Kraft zeichnen
      }   
    ctx.strokeStyle = colorWeight;                         // Farbe f�r Gewichtspfeil
    var y = height-HU-h-5;                                 // y-Koordinate des Anfangspunkts
    if (fG > 0) arrow(XR,y,XR,y+ARRMAX*fG/max,3);          // Falls Gewicht der Last gr��er als 0, Pfeil zeichnen
    }       
  }
  
document.addEventListener("DOMContentLoaded",start,false); // Nach dem Laden der Seite Start-Methode aufrufen


