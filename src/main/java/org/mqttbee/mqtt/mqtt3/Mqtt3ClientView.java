package org.mqttbee.mqtt.mqtt3;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import org.mqttbee.annotations.NotNull;
import org.mqttbee.api.mqtt.mqtt3.Mqtt3Client;
import org.mqttbee.api.mqtt.mqtt3.Mqtt3ClientData;
import org.mqttbee.api.mqtt.mqtt3.message.connect.Mqtt3Connect;
import org.mqttbee.api.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck;
import org.mqttbee.api.mqtt.mqtt3.message.publish.Mqtt3Publish;
import org.mqttbee.api.mqtt.mqtt3.message.publish.Mqtt3PublishResult;
import org.mqttbee.api.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe;
import org.mqttbee.api.mqtt.mqtt3.message.subscribe.Mqtt3SubscribeResult;
import org.mqttbee.api.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import org.mqttbee.api.mqtt.mqtt3.message.unsubscribe.Mqtt3Unsubscribe;
import org.mqttbee.mqtt.message.connect.connack.mqtt3.Mqtt3ConnAckView;
import org.mqttbee.mqtt.message.connect.mqtt3.Mqtt3ConnectView;
import org.mqttbee.mqtt.message.disconnect.mqtt3.Mqtt3DisconnectView;
import org.mqttbee.mqtt.message.publish.mqtt3.Mqtt3PublishResultView;
import org.mqttbee.mqtt.message.publish.mqtt3.Mqtt3PublishView;
import org.mqttbee.mqtt.message.unsubscribe.mqtt3.Mqtt3UnsubscribeView;
import org.mqttbee.mqtt5.Mqtt5ClientImpl;
import org.mqttbee.rx.FlowableWithSingle;
import org.mqttbee.util.MustNotBeImplementedUtil;

/**
 * @author Silvio Giebl
 */
public class Mqtt3ClientView implements Mqtt3Client {

    private final Mqtt5ClientImpl wrapped;

    public Mqtt3ClientView(@NotNull final Mqtt5ClientImpl wrapped) {
        this.wrapped = wrapped;
    }

    @NotNull
    @Override
    public Single<Mqtt3ConnAck> connect(@NotNull final Mqtt3Connect connect) {
        final Mqtt3ConnectView connectView =
                MustNotBeImplementedUtil.checkNotImplemented(connect, Mqtt3ConnectView.class);
        return wrapped.connect(connectView.getWrapped()).map(Mqtt3ConnAckView::create);
    }

    @NotNull
    @Override
    public FlowableWithSingle<Mqtt3SubscribeResult, Mqtt3SubAck, Mqtt3Publish> subscribe(
            @NotNull final Mqtt3Subscribe subscribe) {
        final Mqtt3ConnectView subscribeView =
                MustNotBeImplementedUtil.checkNotImplemented(subscribe, Mqtt3ConnectView.class);

//        return wrapped.subscribe(subscribeView.getWrapped());
        return null;
    }

    @NotNull
    @Override
    public Flowable<Mqtt3Publish> remainingPublishes() {
        return wrapped.remainingPublishes().map(Mqtt3PublishView::create);
    }

    @NotNull
    @Override
    public Flowable<Mqtt3Publish> allPublishes() {
        return wrapped.allPublishes().map(Mqtt3PublishView::create);
    }

    @NotNull
    @Override
    public Completable unsubscribe(@NotNull final Mqtt3Unsubscribe unsubscribe) {
        final Mqtt3UnsubscribeView unsubscribeView =
                MustNotBeImplementedUtil.checkNotImplemented(unsubscribe, Mqtt3UnsubscribeView.class);
        return wrapped.unsubscribe(unsubscribeView.getWrapped()).toCompletable();
    }

    @NotNull
    @Override
    public Flowable<Mqtt3PublishResult> publish(@NotNull final Flowable<Mqtt3Publish> publishFlowable) {
        return wrapped.publish(publishFlowable.map(Mqtt3PublishView::wrapped)).map(Mqtt3PublishResultView::create);
    }

    @NotNull
    @Override
    public Completable disconnect() {
        return wrapped.disconnect(Mqtt3DisconnectView.wrapped());
    }

    @NotNull
    @Override
    public Mqtt3ClientData getClientData() {
        return new Mqtt3ClientDataView(wrapped.getClientData());
    }

}