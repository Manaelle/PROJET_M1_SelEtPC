# UBUNTU: sudo apt-get install pandoc  

MDFILES=$(wildcard *.md)
ORGFILES=$(wildcard *.org)

HTMLFILES=$(MDFILES:.md=.html) $(ORGFILES:.org=.html)

all: $(HTMLFILES)

%.html: %.org
	emacs $< --batch -f org-html-export-to-html --kill

%.html: %.md
	pandoc -s -c style.css -f markdown $< > $@ 


clean:
	rm *.html
