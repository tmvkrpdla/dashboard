package com.dashboard;

import com.dashboard.dao.DataLpDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataLpService {
    private final DataLpDAO dataLpDAO;

    public Long getTotalUsageFifteenMinute(long dtDttmHI) {
        return dataLpDAO.getTotalUsageFifteenMinute(dtDttmHI);
    }
}
