package pro200.smile.service;

import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;

public interface SmileService {

    SmileList GetUserSmiles();
    SmileList GetRandomSmiles(int count);
}
