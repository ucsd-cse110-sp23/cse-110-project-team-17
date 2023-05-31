package project.gui;

public interface AutomaticLogInGUISubject {
    void register(AutomaticLoginGUIObserver algObs);
    void notifyObservers();
}
