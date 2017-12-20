package at.fhv.itm3.s2.roundabout.integration;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.mocks.RouteGenerator;
import at.fhv.itm3.s2.roundabout.mocks.RouteType;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.when;

public class DestinationReachedIntegration {

    private RoundaboutSimulationModel model;
    private Experiment exp;

    @Before
    public void setUp() {
        model = new RoundaboutSimulationModel(null, "", false, false);
        exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);
        exp.setShowProgressBar(false);
    }

    @Test
    public void destinationReached() {
        exp.stop(new TimeInstant(60, TimeUnit.SECONDS));

        RouteGenerator routeGenerator = new RouteGenerator(model);
        IRoute route = routeGenerator.getRoute(RouteType.TWO_STREETSECTIONS_ONE_CAR);
        AbstractSource source = route.getSource();
        Street destinationSection = route.getDestinationSection();

        source.startGeneratingCars();

        exp.start();
        exp.finish();

        if(!destinationSection.isEmpty()){
            if(destinationSection.getFirstCar() != null) {
                Assert.assertEquals("car never reached destination.",destinationSection.getFirstCar().getDestination().getIdentNumber(), destinationSection.getIdentNumber());
            }
        } else {
            Assert.fail();
        }
    }
}

