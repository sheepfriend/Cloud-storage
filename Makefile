default: all

all:  mydedup build haha

build:  ChunkInfo.java DataStore.java Chunks.java FileData.java MyDedup.java ChunkInfoAzure.java ChunkInfoLocal.java FileChunks.java MetaData.java
	javac $^ -d mydedup -cp .:$(AZURE)/*

mydedup:
	mkdir mydedup

haha:
	echo Pakcage name: mydedup. Please use mydedup.MyDedup as the class name

clean:
	rm -rf mydedup

