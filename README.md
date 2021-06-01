# JavaProject

Proiectul are scopul de a executa un atac avand doar un criptotext pentru care se stie ca a fost criptat cu algoritmul Vigenere.

Cum se utilizeaza:
	- se construieste un text in limba engleza care sa aiba sens
	- cu cat este mai lung textul si are mai multe cuvinte, cu atat este mai precis codul
	- codul va cripta textul respectiv folosind o cheie generata random si va incerca sa execute un atac folosind doar criptotextul
	- in cazul unui succes, algoritmul regenereaza o alta cheie pentru care incearca atacul respectiv
	- in cazul unui esec, algoritmul se opreste

Se va observa ca in cazul unui esec, cheia returnata are lungimea 1 deoarece metoda care cauta lungimea cheii foloseste niste constrageri care nu sunt cele mai bune pentru cheia aleator
generata pe care se executa testul. (A se vedea exemplul cu key="osrv")

Clasa Generator si Crypto au fost implementate folosind design pattern-ul Singleton.

In clasa Main, intr-o bucla infinita se genereaza o cheie random folosita sa codifice textul din fisier, iar apoi se incearca spargerea cheii respective.

Clasa Generator are urmatoarele metode:
* generateRandomKey()
	- genereaza o cheie random de maxim 20 de caractere (litere mici)

* getEnglishString()
	- prelucreaza textul din fisier si elimina spatiile, iar literele mari le transforma in litere mici

* readEnglishString()
	- citeste textul din fisier

Clasa Crypto are urmatoarele metode:
* encryptVigenere(String plainText, String key)
	- Va cripta string-ul plainText folosind cheia key cu ajutorul algoritmului lui Vigenere
	- https://en.wikipedia.org/wiki/Vigen%C3%A8re_cipher

* loadFreq(double[] freq)
	- Calculeaza valorile de distributie ale literelor in limba engleza (https://en.wikipedia.org/wiki/Letter_frequency)
	- Pentru o acuratete mai buna, se ia textul dat ca input si se calculeaza ad-hoc valorile respective

* decryptVigenere(String cryptedText)
	- Aceasta metoda executa atacul, avand doar criptotextul si returneaza cheia gasita cu care textul a fost criptat
	- In derularea atacului, este folosit indexul mutual de coincidenta (https://crypto.stackexchange.com/questions/70217/mutual-index-of-coincidence)

* computeVigenereKeyLength(String cryptedText)
	- Determina lungimea cheii cu ajutorul indexului de coincidenta (https://en.wikipedia.org/wiki/Index_of_coincidence)

* computeMutualIndexOfCoincidence(String text, double[] freq, int shift)
	- Calculeaza indexul mutual de coincidenta pentru textul text shiftat cu shift caractere la dreapta, stiind distributia freq

* computeIndexOfCoincidence(String text)
	- calculeaza indexul de coincidenta pentru un text dat

* shiftCharacter(char plainChar, char keyChar)
	- muta caracterul plainChar cu keyChar caractere mai la dreapta, unde caracterul 'a' este 0, 'b' este 1 etc.