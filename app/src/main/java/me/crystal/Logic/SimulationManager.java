package me.crystal.Logic;

import kaptainwutax.mcutils.block.Blocks;
import me.crystal.net.minecraft.entity.boss.dragon.EnderDragonEntity;
import me.crystal.net.minecraft.entity.boss.dragon.phase.PhaseType;
import me.crystal.net.minecraft.util.math.BlockPos;
import me.crystal.net.minecraft.world.end.DragonFightManager;

import javax.swing.SwingWorker;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SimulationManager {
    private final int numSims;
    private final String mode;
    private volatile boolean running = false;
    private static final int TICK_BUFFER = 300;
    private static final Logger LOGGER = Logger.getLogger(SimulationManager.class.getName());

    public SimulationManager(int numSims, String mode) {
        this.numSims = numSims;
        this.mode = mode;
    }

    public String startSimulation(String seeds) {
        if (running) {
            return "Error: Simulation is already running.";
        }

        running = true;
        return runSimulation(seeds);
    }

    private String runSimulation(String seeds) {
        List<Long> seedList = parseSeeds(seeds);
        if (seedList == null) {
            return "Error: Invalid seeds provided.";
        }

        // Initialize the simulation worker
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                int arraySize = 1600 + TICK_BUFFER;
                int[][] bedData = new int[seedList.size()][arraySize];

                int totalSimulations = seedList.size() * numSims;
                int progressInterval = totalSimulations / 20;  // 5% increment for progress updates
                int simulationsCompleted = 0;

                for (int seedNum = 0; seedNum < seedList.size() && running; ++seedNum) {
                    try {
                        DragonFightManager dragonFight = new DragonFightManager(seedList.get(seedNum));
                        DragonFightManager.world.put(DragonFightManager.preHash(new BlockPos(0, dragonFight.FountainHeight, 0)), Blocks.OBSIDIAN);

                        for (int count = 0; count < numSims && running; ++count) {
                            EnderDragonEntity dragon = dragonFight.createNewDragon();
                            int tick = 20;
                            while (!dragon.getPhaseManager().getCurrentPhase().getType().equals(PhaseType.TAKEOFF)) {
                                dragon.livingTick();
                                ++tick;
                            }

                            if (tick + (mode.equals("onecycle") ? 171 : 202) < bedData[seedNum].length) {
                                ++bedData[seedNum][tick + (mode.equals("onecycle") ? 171 : 202)];
                            } else {
                                publish("Warning: Tick index " + (tick + (mode.equals("onecycle") ? 171 : 202)) + " out of bounds.");
                            }

                            simulationsCompleted++;
                            if (simulationsCompleted % progressInterval == 0) {
                                int percentCompleted = (int) ((double) simulationsCompleted / totalSimulations * 100);
                                int simulationsLeft = totalSimulations - simulationsCompleted;
                                publish(String.format("%d%% of the total number of simulations have been completed (%d simulations still left to complete)", percentCompleted, simulationsLeft));
                            }
                        }
                    } catch (Exception e) {
                        publish("Error during simulation for seed " + seedList.get(seedNum) + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                // Print results to console instead of file
                printResults(bedData);

                publish("Simulation complete.");
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String message : chunks) {
                    // Print progress updates to the console
                    System.out.println(message);
                }
            }

            @Override
            protected void done() {
                running = false;
                System.out.println("Simulation finished.");
                LOGGER.info("Simulation finished.");
            }
        };

        worker.execute();
        return "Simulation started.";
    }

    private List<Long> parseSeeds(String seeds) {
        List<Long> seedList = new ArrayList<>();
        String[] seedStrings = seeds.split(",");
        for (String seedStr : seedStrings) {
            try {
                seedList.add(Long.parseLong(seedStr.trim()));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return seedList;
    }

    private void printResults(int[][] bedData) {
        for (int ss = 30; ss < 67; ++ss) {
            for (int cs = 0; cs < 100; cs += 5) {
                System.out.printf("%02d.%02d,", ss, cs);
            }
        }
        System.out.println("67.00");
        for (int[] bedDatum : bedData) {
            for (int tick = 600; tick <= 1340; ++tick) {
                if (tick < bedDatum.length) {
                    System.out.printf("%f,", bedDatum[tick] / (double) numSims);
                } else {
                    System.out.print("0,");
                }
            }
            System.out.println();
        }
    }
}
