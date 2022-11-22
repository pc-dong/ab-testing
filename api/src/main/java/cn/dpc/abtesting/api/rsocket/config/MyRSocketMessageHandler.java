package cn.dpc.abtesting.api.rsocket.config;


import cn.dpc.abtesting.api.rsocket.config.annotation.DeleteMessageMapping;
import cn.dpc.abtesting.api.rsocket.config.annotation.GetMessageMapping;
import cn.dpc.abtesting.api.rsocket.config.annotation.PostMessageMapping;
import cn.dpc.abtesting.api.rsocket.config.annotation.PutMessageMapping;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.messaging.handler.CompositeMessageCondition;
import org.springframework.messaging.handler.DestinationPatternsMessageCondition;
import org.springframework.messaging.rsocket.annotation.support.RSocketFrameTypeMessageCondition;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.util.StringUtils;
import reactor.util.annotation.Nullable;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MyRSocketMessageHandler extends RSocketMessageHandler {
    private static final String GET = "get";
    private static final String POST = "post";
    private static final String DELETE = "delete";
    private static final String PUT = "put";

    @Override
    @Nullable
    protected CompositeMessageCondition getCondition(AnnotatedElement element) {
        CompositeMessageCondition condition = super.getCondition(element);
        if(null != condition) {
            return condition;
        }

        GetMessageMapping getMessageMappingAnn = AnnotatedElementUtils.findMergedAnnotation(element, GetMessageMapping.class);
        if (getMessageMappingAnn != null) {
            return getCompositeMessageCondition(getMessageMappingAnn.value(), GET);
        }

        PostMessageMapping postMessageMappingAnn = AnnotatedElementUtils.findMergedAnnotation(element, PostMessageMapping.class);
        if (postMessageMappingAnn != null) {
            return getCompositeMessageCondition(postMessageMappingAnn.value(), POST);
        }

        DeleteMessageMapping deleteMessageMappingAnn = AnnotatedElementUtils.findMergedAnnotation(element, DeleteMessageMapping.class);
        if (deleteMessageMappingAnn != null) {
            return getCompositeMessageCondition(deleteMessageMappingAnn.value(), DELETE);
        }

        PutMessageMapping putMessageMappingAnn = AnnotatedElementUtils.findMergedAnnotation(element, PutMessageMapping.class);
        if (putMessageMappingAnn != null) {
            return getCompositeMessageCondition(putMessageMappingAnn.value(), PUT);
        }

        return null;
    }

    private CompositeMessageCondition getCompositeMessageCondition(String[] value, String suffix) {
        String[] resultValue = value.length == 0 ? new String[]{suffix} : Arrays.stream(value)
                .map(item -> item + (StringUtils.hasLength(item) ? "." : "") + suffix)
                .collect(Collectors.toList())
                .toArray(new String[]{});
        return new CompositeMessageCondition(
                RSocketFrameTypeMessageCondition.EMPTY_CONDITION,
                new DestinationPatternsMessageCondition(processDestinations(resultValue), obtainRouteMatcher()));
    }
}
