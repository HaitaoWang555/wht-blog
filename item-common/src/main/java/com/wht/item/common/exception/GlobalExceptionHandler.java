package com.wht.item.common.exception;

import com.wht.item.common.api.CommonResult;
import com.wht.item.common.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局异常处理
 * Created by wht on 2020/2/27.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 运行时异常
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param re  {@link RuntimeException}
     * @return {@link CommonResult}
     */
    @ExceptionHandler(RuntimeException.class)
    public CommonResult runtimeExceptionHandler(HttpServletRequest req, HttpServletResponse rep, RuntimeException re) {
        log.error("---RuntimeException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), re.getMessage(), re);
        rep.setStatus(ResultCode.RUNTIME.getCode());
        return CommonResult.failed(ResultCode.RUNTIME);
    }

    /**
     * 空指针异常
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link NullPointerException}
     * @return {@link CommonResult}
     */
    @ExceptionHandler(NullPointerException.class)
    public CommonResult nullPointerExceptionHandler(HttpServletRequest req, HttpServletResponse rep, NullPointerException ex) {
        log.error("---NullPointerException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ResultCode.NULL_POINTER.getCode());
        return CommonResult.failed(ResultCode.NULL_POINTER);
    }

    /**
     * 类型转换异常
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link ClassCastException}
     * @return {@link CommonResult}
     */
    @ExceptionHandler(ClassCastException.class)
    public CommonResult classCastExceptionHandler(HttpServletRequest req, HttpServletResponse rep, ClassCastException ex) {
        log.error("---classCastException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ResultCode.CLASS_CAST.getCode());
        return CommonResult.failed(ResultCode.CLASS_CAST);
    }

    /**
     * IO异常
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link IOException}
     * @return {@link CommonResult}
     */
    @ExceptionHandler(IOException.class)
    public CommonResult classCastExceptionHandler(HttpServletRequest req, HttpServletResponse rep, IOException ex) {
        log.error("---classCastException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ResultCode.IO.getCode());
        return CommonResult.failed(ResultCode.IO);
    }

    /**
     * 未知方法异常
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link NoSuchMethodException}
     * @return {@link CommonResult}
     */
    @ExceptionHandler(NoSuchMethodException.class)
    public CommonResult noSuchMethodExceptionHandler(HttpServletRequest req, HttpServletResponse rep, NoSuchMethodException ex) {
        log.error("---noSuchMethodException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ResultCode.NO_SUCH_METHOD.getCode());
        return CommonResult.failed(ResultCode.NO_SUCH_METHOD);
    }

    /**
     * 数组越界异常
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link IndexOutOfBoundsException}
     * @return {@link CommonResult}
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public CommonResult indexOutOfBoundsExceptionHandler(HttpServletRequest req, HttpServletResponse rep, IndexOutOfBoundsException ex) {
        log.error("---indexOutOfBoundsException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ResultCode.INDEX_OUTOF_BOUNDS.getCode());
        return CommonResult.failed(ResultCode.INDEX_OUTOF_BOUNDS);
    }

    /**
     * 400相关异常
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link CommonResult}
     */
    @ExceptionHandler({HttpMessageNotReadableException.class, TypeMismatchException.class, MissingServletRequestParameterException.class})
    public CommonResult request400(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---request400 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ResultCode.BAD_REQUEST.getCode());
        return CommonResult.failed(ResultCode.BAD_REQUEST);
    }

    /**
     * 404异常
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link CommonResult}
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public CommonResult request404(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---request404 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ResultCode.NOT_FOUND.getCode());
        return CommonResult.failed(ResultCode.NOT_FOUND);
    }

    /**
     * 405相关异常
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link CommonResult}
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public CommonResult request405(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---request405 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ResultCode.METHOD_BOT_ALLOWED.getCode());
        return CommonResult.failed(ResultCode.METHOD_BOT_ALLOWED);
    }

    /**
     * 406相关异常
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link CommonResult}
     */
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public CommonResult request406(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---request406 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ResultCode.NOT_ACCEPTABLE.getCode());
        return CommonResult.failed(ResultCode.NOT_ACCEPTABLE);
    }

    /**
     * 500相关异常
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link CommonResult}
     */
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public CommonResult server500(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---server500 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        rep.setStatus(ResultCode.INTERNAL_SERVER_ERROR.getCode());
        return CommonResult.failed(ResultCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 全局异常返回
     *
     * @param req {@link HttpServletRequest}
     * @param e   {@link HttpServletResponse}
     * @return {@link Exception}
     */
    @ExceptionHandler(value = Exception.class)
    public CommonResult defaultErrorHandler(HttpServletRequest req, HttpServletResponse rep, Exception e) {
        log.error("---DefaultException Handler---Host {}, invokes url {}, ERROR TYPE: {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), e.getClass(), e.getMessage());
        return CommonResult.failed(e.getMessage());
    }
}
