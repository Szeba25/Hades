package hu.szeba.hades.controller.task;

import java.io.IOException;

public interface Work {

    void execute(Publisher publisher) throws IOException, InterruptedException;

}