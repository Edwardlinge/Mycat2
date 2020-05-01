package io.mycat.commands;

import io.mycat.MycatDataContext;
import io.mycat.client.MycatRequest;
import io.mycat.util.Response;

public class OkCommand implements MycatCommand{
    @Override
    public boolean run(MycatRequest request, MycatDataContext context, Response response) {
         response.sendOk();
         return true;
    }

    @Override
    public boolean explain(MycatRequest request, MycatDataContext context, Response response) {
        response.sendExplain(OkCommand.class,"ok");
        return true;
    }

    @Override
    public String getName() {
        return "ok";
    }
}