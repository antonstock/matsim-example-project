package org.matsim.prepare;

import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.contrib.osm.networkReader.SupersonicOsmNetworkReader;
import org.matsim.core.network.algorithms.NetworkCleaner;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateNetworkFromOsm {

    private static final String UTM32nAsEpsg = "EPSG:25832";
    private static final Path input = Paths.get("C:\\Users\\Janekdererste\\Downloads\\bremen-latest.osm.pbf");

    public static void main(String[] args) {
        new CreateNetworkFromOsm().create();
    }

    private void create() {

        // choose an appropriate coordinate transformation. OSM Data is in WGS84. When working in central Germany,
        // EPSG:25832 or EPSG:25833 as target system is a good choice
        CoordinateTransformation transformation = TransformationFactory.getCoordinateTransformation(
                TransformationFactory.WGS84, UTM32nAsEpsg
        );


        // create an osm network reader with a filter
        SupersonicOsmNetworkReader reader = new SupersonicOsmNetworkReader.Builder()
                .setCoordinateTransformation(transformation)
                .build();

        // the actual work is done in this call. Depending on the data size this may take a long time
        Network network = reader.read(input.toString());

        // clean the network to remove unconnected parts where agents might get stuck
        new NetworkCleaner().run(network);

        // write out the network into a file
        new NetworkWriter(network).write("C:\\Users\\Janekdererste\\Desktop\\class-network.xml.gz");
    }
}
