package org.mqttbee.mqtt5.handler.disconnect;

import io.netty.channel.Channel;
import org.mqttbee.annotations.NotNull;
import org.mqttbee.annotations.Nullable;
import org.mqttbee.api.mqtt.mqtt5.exceptions.Mqtt5MessageException;
import org.mqttbee.api.mqtt.mqtt5.message.disconnect.Mqtt5DisconnectReasonCode;
import org.mqttbee.mqtt.codec.encoder.mqtt5.Mqtt5DisconnectEncoder;
import org.mqttbee.mqtt.datatypes.MqttUTF8StringImpl;
import org.mqttbee.mqtt.datatypes.MqttUserPropertiesImpl;
import org.mqttbee.mqtt.message.disconnect.MqttDisconnect;
import org.mqttbee.mqtt5.netty.ChannelAttributes;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Util for sending a DISCONNECT message and channel closing from the client side. Fires {@link ChannelCloseEvent}s.
 *
 * @author Silvio Giebl
 */
@Singleton
public class Mqtt5Disconnecter implements MqttDisconnecter {

    @Inject
    Mqtt5Disconnecter() {
    }

    @Override
    public void disconnect(
            @NotNull final Channel channel, final Mqtt5DisconnectReasonCode reasonCode,
            @NotNull final String reasonString) {

        final MqttDisconnect disconnect = createDisconnect(channel, reasonCode, reasonString);
        MqttDisconnectUtil.fireChannelCloseEvent(channel, new Mqtt5MessageException(disconnect, reasonString));
        MqttDisconnectUtil.disconnectAndClose(channel, disconnect);
    }

    @Override
    public void disconnect(
            @NotNull final Channel channel, final Mqtt5DisconnectReasonCode reasonCode,
            @NotNull final Throwable cause) {

        final MqttDisconnect disconnect = createDisconnect(channel, reasonCode, cause.getMessage());
        MqttDisconnectUtil.fireChannelCloseEvent(channel, new Mqtt5MessageException(disconnect, cause));
        MqttDisconnectUtil.disconnectAndClose(channel, disconnect);
    }

    private MqttDisconnect createDisconnect(
            @NotNull final Channel channel, final Mqtt5DisconnectReasonCode reasonCode,
            @Nullable final String reasonString) {

        MqttUTF8StringImpl mqttReasonString = null;
        if ((reasonString != null) && ChannelAttributes.sendReasonString(channel)) {
            mqttReasonString = MqttUTF8StringImpl.from(reasonString);
        }

        return new MqttDisconnect(reasonCode, MqttDisconnect.SESSION_EXPIRY_INTERVAL_FROM_CONNECT, null,
                mqttReasonString, MqttUserPropertiesImpl.NO_USER_PROPERTIES, Mqtt5DisconnectEncoder.PROVIDER);
    }

}
