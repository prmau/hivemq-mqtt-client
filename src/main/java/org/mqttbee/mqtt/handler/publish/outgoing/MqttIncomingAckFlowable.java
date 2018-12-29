/*
 * Copyright 2018 The MQTT Bee project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.mqttbee.mqtt.handler.publish.outgoing;

import io.reactivex.Flowable;
import io.reactivex.internal.subscriptions.EmptySubscription;
import org.jetbrains.annotations.NotNull;
import org.mqttbee.api.mqtt.exceptions.NotConnectedException;
import org.mqttbee.api.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import org.mqttbee.mqtt.MqttClientConfig;
import org.mqttbee.mqtt.ioc.ClientComponent;
import org.mqttbee.mqtt.message.publish.MqttPublish;
import org.reactivestreams.Subscriber;

/**
 * @author Silvio Giebl
 */
public class MqttIncomingAckFlowable extends Flowable<Mqtt5PublishResult> {

    private final @NotNull Flowable<MqttPublish> publishFlowable;
    private final @NotNull MqttClientConfig clientConfig;

    public MqttIncomingAckFlowable(
            final @NotNull Flowable<MqttPublish> publishFlowable, final @NotNull MqttClientConfig clientConfig) {

        this.publishFlowable = publishFlowable;
        this.clientConfig = clientConfig;
    }

    @Override
    protected void subscribeActual(final @NotNull Subscriber<? super Mqtt5PublishResult> subscriber) {
        if (clientConfig.getState().isConnectedOrReconnect()) {
            final ClientComponent clientComponent = clientConfig.getClientComponent();
            final MqttOutgoingQosHandler outgoingQosHandler = clientComponent.outgoingQosHandler();
            final MqttPublishFlowables publishFlowables = outgoingQosHandler.getPublishFlowables();

            final MqttIncomingAckFlow flow = new MqttIncomingAckFlow(subscriber, outgoingQosHandler);
            subscriber.onSubscribe(flow);
            publishFlowables.add(new MqttPublishFlowableAckLink(publishFlowable, flow));
        } else {
            EmptySubscription.error(new NotConnectedException(), subscriber);
        }
    }
}