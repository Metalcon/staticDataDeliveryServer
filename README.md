Static Data delivery
========
the static data delivery component is a persistent graph store that creates a persistent cache for certain graph queries. It is designed for systems with which only expect a view write requests but a high throughput on read requests.


## getting started
in order to use the static data delivery component you have to create the folder `/usr/share/sdd/` and own it with your user. LevelDB and neo4j data stores will be placed in this folder.


find more (mostly outdated information) in our wiki at: https://github.com/renepickhardt/metalcon/wiki/componentStaticDataDelivery

To generate java classes that can hold  sdd ouput do:

    ./classGenerator.sh /usr/share/sdd/config.xml /home/lukas/metalcon/workspace/middleware/src/main/java/de/metalcon/middleware/sdd de.metalcon.middleware.sdd
