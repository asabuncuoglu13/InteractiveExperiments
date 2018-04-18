package alpay.com.interactiveexperiments.util;

import android.support.v4.app.Fragment;

import alpay.com.interactiveexperiments.camera.TeachingCardsFragment;
import alpay.com.interactiveexperiments.chat.ChatFragment;
import alpay.com.interactiveexperiments.home.HomeFragment;
import alpay.com.interactiveexperiments.simulation.SimulationParameters;
import alpay.com.interactiveexperiments.simulation.simulation_fragments.ConstantAccelerationSimulationFragment;
import alpay.com.interactiveexperiments.simulation.simulation_fragments.InclinedPlaneCanvasFragment;
import alpay.com.interactiveexperiments.simulation.simulation_fragments.InclinedPlaneSimulationFragment;
import alpay.com.interactiveexperiments.simulation.simulation_fragments.LeverSimulationFragment;
import alpay.com.interactiveexperiments.simulation.simulation_fragments.OhmsLawSimulationFragment;
import alpay.com.interactiveexperiments.simulation.simulation_fragments.PulleySimulationFragment;
import alpay.com.interactiveexperiments.simulation.simulation_fragments.SimulationListFragment;
import alpay.com.interactiveexperiments.studynotes.StudyNotesFragment;

public class FragmentManager {


    public enum FRAGMENT_TYPE {
        HOME_FRAGMENT {
            @Override
            public Fragment getFragment() {
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            }

            @Override
            public int getFragmentID() {
                return Category.HOME.id;
            }
        },
        TEACHINGCARDS_FRAGMENT {
            @Override
            public Fragment getFragment() {
                TeachingCardsFragment teachingCardsFragment = new TeachingCardsFragment();
                return teachingCardsFragment;
            }

            @Override
            public int getFragmentID() {
                return Category.TEACHINGCARDS.id;
            }
        },
        CHAT_FRAGMENT {
            @Override
            public Fragment getFragment() {
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            }

            @Override
            public int getFragmentID() {
                return Category.CHAT.id;
            }
        },
        SIMULATION_LIST_FRAGMENT {
            @Override
            public Fragment getFragment() {
                SimulationListFragment simulationListFragment = new SimulationListFragment();
                return simulationListFragment;
            }

            @Override
            public int getFragmentID() {
                return Category.SIMULATION.id;
            }
        },
        STUDY_NOTES_FRAGMENT {
            @Override
            public Fragment getFragment() {
                StudyNotesFragment studyNotesFragment = new StudyNotesFragment();
                return studyNotesFragment;
            }

            @Override
            public int getFragmentID() {
                return Category.NOTE.id;
            }
        },
        INCLINEDPLANESIMULATION_FRAGMENT {
            @Override
            public Fragment getFragment() {
                InclinedPlaneSimulationFragment inclinedPlaneSimulationFragment = new InclinedPlaneSimulationFragment();
                return inclinedPlaneSimulationFragment;
            }

            @Override
            public int getFragmentID() {
                return SimulationParameters.INCLINED_PLANE_SIMULATION;
            }
        },
        INCLINEDPLANECANVAS_FRAGMENT {
            @Override
            public Fragment getFragment() {
                InclinedPlaneCanvasFragment inclinedPlaneCanvasFragment = new InclinedPlaneCanvasFragment();
                return inclinedPlaneCanvasFragment;
            }

            @Override
            public int getFragmentID() {
                return SimulationParameters.INCLINED_CANVAS_SIMULATION;
            }
        },
        CONSTANTACCELERATIONSIMULATION_FRAGMENT {
            @Override
            public Fragment getFragment() {
                ConstantAccelerationSimulationFragment constantAccelerationSimulationFragment = new ConstantAccelerationSimulationFragment();
                return constantAccelerationSimulationFragment;
            }

            @Override
            public int getFragmentID() {
                return SimulationParameters.CONSTANT_ACCELERATION_SIMULATION;
            }
        },
        OHMSLAWSIMULATION_FRAGMENT {
            @Override
            public Fragment getFragment() {
                OhmsLawSimulationFragment ohmsLawSimulationFragment = new OhmsLawSimulationFragment();
                return ohmsLawSimulationFragment;
            }

            @Override
            public int getFragmentID() {
                return SimulationParameters.OHMS_LAW_SIMULATION;
            }
        },
        PULLEYSIMULATION_FRAGMENT {
            @Override
            public Fragment getFragment() {
                PulleySimulationFragment pulleySimulationFragment = new PulleySimulationFragment();
                return pulleySimulationFragment;
            }

            @Override
            public int getFragmentID() {
                return SimulationParameters.PULLEY_SIMULATION;
            }
        },
        LEVERSIMULATION_FRAGMENT {
            @Override
            public Fragment getFragment() {
                LeverSimulationFragment leverSimulationFragment = new LeverSimulationFragment();
                return leverSimulationFragment;
            }

            @Override
            public int getFragmentID() {
                return SimulationParameters.LEVER_SIMULATION;
            }
        };

        public abstract Fragment getFragment();

        public abstract int getFragmentID();
    }

    public enum Category {
        CHAT(1),
        SIMULATION(2),
        NOTE(4),
        HOME(5),
        LOGIN(6),
        LOGOUT(7),
        BLOCKLY(8),
        TEACHINGCARDS(9),
        SENDFEEDBACK(10);
        public final int id;
        public static int currentCategoryID;

        Category(int id) {
            this.id = id;
        }
    }
}
