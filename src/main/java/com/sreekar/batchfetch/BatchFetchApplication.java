package com.sreekar.batchfetch;

import com.sreekar.batchfetch.dao.MySqlConnection;
import com.sreekar.batchfetch.dao.UsersDao;
import com.sreekar.batchfetch.models.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class BatchFetchApplication {

    private static final Logger Log = Logger.getLogger(BatchFetchApplication.class.getName());

    public static void main(String[] args) throws IOException, SQLException {
        MySqlConnection mySqlConnection = MySqlConnection.getInstance();

        UsersDao usersDao = new UsersDao(mySqlConnection.getConnection());
        int countUsers = usersDao.getUsersCount();
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        List<User> aggregatedUsers = new LinkedList<>();
        long start = System.currentTimeMillis();
        usersDao.getUsers();
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();


        ExecutorCompletionService<List<User>> executorCompletionService = new ExecutorCompletionService<>(executorService);

        List<Callable<List<User>>> callables = new LinkedList<>();
        int perThread = 10000;

        int j = perThread, i = 0;
        for (; j < countUsers; j += perThread) {
            int finalI = i;
            int finalJ = j;
            callables.add(() -> usersDao.getUsers(finalI, finalJ));
            i = j;
        }
        int finalI = i;
        callables.add(() -> usersDao.getUsers(finalI, countUsers));
        callables.forEach(executorCompletionService::submit);


        IntStream.range(0, callables.size()).forEach((index) -> {
            try {
                aggregatedUsers.addAll(executorCompletionService.take().get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });


        awaitTerminationAfterShutdown(executorService);

        System.out.println(System.currentTimeMillis() - start);

    }

    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
