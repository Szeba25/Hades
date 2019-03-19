package hu.szeba.hades.main.controller.task;

import java.io.IOException;

public interface Work {

    void execute(Publisher publisher) throws IOException, InterruptedException;

}
