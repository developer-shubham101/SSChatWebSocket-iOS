package in.newdevpoint.ssnodejschat.observer;// This interface handles adding, deleting and updating
// all observers 

public interface DownloadSubject {

    void register(WebSocketObserver o);

    void unregister(WebSocketObserver o);

    void notifyObserver(String response);

}