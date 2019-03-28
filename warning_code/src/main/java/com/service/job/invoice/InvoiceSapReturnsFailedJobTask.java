package com.service.job.invoice;

import com.ds.DS;
import com.enums.WarningEnum;
import com.service.warn.TcErrorMsgService;
import com.service.warn.WarningSendMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Auther: hsl
 * @Date: 2018/9/26 10:46
 * @Description:发票接口，SAP返回失败（YJ00047）
 */
@Component
public class InvoiceSapReturnsFailedJobTask {

    public final static Logger _logger = LoggerFactory.getLogger(InvoiceSapReturnsFailedJobTask.class);


    @Autowired
    private TcErrorMsgService tcErrorMsgService;
    @Autowired
    private WarningSendMsgService warningSendMsgService;


    @Scheduled(cron = "0 30 6,11,13,15,21 * * ?")
    @DS(value = "erp_tc")
    public void getErrorMsg() {
        _logger.info("开始抓取>发票接口，SAP返回失败信息>>>");
        try {
            tcErrorMsgService.dealTcErrorMsg(WarningEnum.WARNING_IT_INVOICE_SAP_RETURNS_FAILED.getId(), false);
        } catch (Exception e) {
            _logger.error("抓取>发票接口，SAP返回失败信息>失败!!!" + e.getMessage());
        }
        _logger.info("抓取>发票接口，SAP返回失败信息>结束......");
    }

    /**
     * 推送消息
     */
    @Scheduled(cron = "0 */12 * * * ?")
    @DS(value = "tc_warning")
    public void sendMsg() {
        _logger.info("开始推送>-发票接口，SAP返回失败信息 >>>");
        try {
            warningSendMsgService.updateWarnAndSendMsg(WarningEnum.WARNING_IT_INVOICE_SAP_RETURNS_FAILED.getId());
        } catch (Exception e) {
            _logger.error("推送>-发票接口，SAP返回失败信息>失败!!!" + e.getMessage());
        }
        _logger.info("推送>-发票接口，SAP返回失败信息>结束......");
    }
}
