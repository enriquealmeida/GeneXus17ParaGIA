using System;
using System.Collections;
using System.Web.Services.Protocols;
using System.Reflection;
using System.Xml.Serialization;
using System.ServiceModel;
using System.ServiceModel.Activation;
using System.Runtime.Serialization;
using System.ServiceModel.Dispatcher;
using System.ServiceModel.Description;
using System.ServiceModel.Configuration;
using System.ServiceModel.Channels;
using System.Linq;
using System.Xml;
using GeneXus.Utils;
using GeneXus.Application;

namespace #1 
{
    [Serializable]
    // Endpoint behavior  

    public class GxEndpointBehavior : IEndpointBehavior
    {
        public void AddBindingParameters(ServiceEndpoint endpoint, System.ServiceModel.Channels.BindingParameterCollection bindingParameters)
        {
            // No implementation necessary
        }

        public void ApplyClientBehavior(ServiceEndpoint endpoint, ClientRuntime clientRuntime)
        {
            GxLocation loc = GxHelpers.GetLocation(clientRuntime.ContractName);
            if (GxHelpers.UsingWSAddressing(loc))
            {
                // Enable a soap binding that allows WSAddressing
                endpoint.Binding = new CustomBinding("gxWsSoapBinding");
                clientRuntime.ManualAddressing = true;
            }
            clientRuntime.MessageInspectors.Add(new GxeMessageInspector(loc));
        }

        public void ApplyDispatchBehavior(ServiceEndpoint endpoint, EndpointDispatcher endpointDispatcher)
        {
            // No implementation necessary
        }

        public void Validate(ServiceEndpoint endpoint)
        {
            // No implementation necessary
        }
    }

    // Client message inspector  
    public class GxeMessageInspector : IClientMessageInspector
    {
        GxLocation gxLocation;
        public GxeMessageInspector(GxLocation loc)
        {
            this.gxLocation = loc;
        }
        public void AfterReceiveReply(ref System.ServiceModel.Channels.Message reply, object correlationState)
        {
            // For debugging
            //Console.WriteLine(string.Format("Received message ({0}): {1}", correlationState, reply.ToString()));
        }

        public object BeforeSendRequest(ref System.ServiceModel.Channels.Message request, IClientChannel channel)
        {
            if (gxLocation != null && gxLocation.WSAddressing != null)
            {
                if (gxLocation.WSAddressing.Action != null)
                    request.Headers.Action = gxLocation.WSAddressing.Action;
                if (gxLocation.WSAddressing.FaultTo != null)
                    request.Headers.FaultTo = GxHelpers.BuildEndpointAddress(gxLocation.WSAddressing.FaultTo);
                if (gxLocation.WSAddressing.From != null)
                    request.Headers.From = GxHelpers.BuildEndpointAddress(gxLocation.WSAddressing.From);
                if (!string.IsNullOrEmpty(gxLocation.WSAddressing.MessageID))
                    request.Headers.MessageId = new UniqueId(gxLocation.WSAddressing.MessageID);
                if (!string.IsNullOrEmpty(gxLocation.WSAddressing.RelatesTo))
                    request.Headers.RelatesTo = new UniqueId(gxLocation.WSAddressing.RelatesTo);
                if (gxLocation.WSAddressing.ReplyTo != null)
                    request.Headers.ReplyTo = GxHelpers.BuildEndpointAddress(gxLocation.WSAddressing.ReplyTo);
                if (!string.IsNullOrEmpty(gxLocation.WSAddressing.To))
                    request.Headers.To = new Uri(gxLocation.WSAddressing.To);
            }
            Guid guid = Guid.NewGuid();

            // For debugging
            //Console.WriteLine(string.Format("Send message ({0}): {1}", guid, request.ToString()));

            return guid;
        }
    }

    // Configuration element   
    public class GxBehaviorExtensionElement : BehaviorExtensionElement
    {
        public override Type BehaviorType
        {
            get { return typeof(GxEndpointBehavior); }
        }

        protected override object CreateBehavior()
        {
            return new GxEndpointBehavior();
        }
    }

    public class GxServiceBehavior : IServiceBehavior
    {
        public void AddBindingParameters(ServiceDescription description, ServiceHostBase serviceHostBase, System.Collections.ObjectModel.Collection<ServiceEndpoint> endpoints, System.ServiceModel.Channels.BindingParameterCollection parameters)
        {
            // No implementation necessary
        }

        public void ApplyDispatchBehavior(ServiceDescription description, ServiceHostBase serviceHostBase)
        {
            foreach (ChannelDispatcher chDisp in serviceHostBase.ChannelDispatchers)
            {
                foreach (EndpointDispatcher epDisp in chDisp.Endpoints)
                {
                    epDisp.DispatchRuntime.MessageInspectors.Add(new GxServiceMessageInspector(null));
                }
            }
        }

        public void Validate(ServiceDescription description, ServiceHostBase serviceHostBase)
        {
            // No implementation necessary
        }
    }

    // Service message inspector
    public class GxServiceMessageInspector : IDispatchMessageInspector
    {
        GxLocation gxLocation;
        public GxServiceMessageInspector(GxLocation loc)
        {
            this.gxLocation = loc;
        }
        public object AfterReceiveRequest(ref System.ServiceModel.Channels.Message request, IClientChannel channel, InstanceContext instanceContext)
        {
            GxHelpers.GetSoapContext().AppendXml(request.ToString());
            GxHelpers.GetSoapContext().EndMessage();
            return null;
        }

        public void BeforeSendReply(ref System.ServiceModel.Channels.Message reply, object correlationState)
        {
        }
    }

    // Configuration element   
    public class GxServiceBehaviorExtensionElement : BehaviorExtensionElement
    {
        public override Type BehaviorType
        {
            get { return typeof(GxServiceBehavior); }
        }

        protected override object CreateBehavior()
        {
            return new GxServiceBehavior();
        }
    }

    public class GxHelpers
    {
        internal static EndpointAddress BuildEndpointAddress(GXWSAddressingEndPoint gxEndpointAddress)
        {
            if (!string.IsNullOrEmpty(gxEndpointAddress.Address))
            {
                EndpointAddress endpointAddress = new EndpointAddress(new Uri(gxEndpointAddress.Address));
            }
            return null;
        }

        internal static GxLocation GetLocation(string contractName)
        {
            if (GxContext.Current.colLocations != null)
            {
            string locName = contractName.Substring(4);
            return GxContext.Current.colLocations.GetItem(locName);
            }
            return null;
        }

        internal static bool UsingWSAddressing(GxLocation gxLocation)
        {
            return gxLocation != null && gxLocation.WSAddressing != null && !string.IsNullOrEmpty(gxLocation.WSAddressing.MessageID);
        }
        internal static GXSOAPContext GetSoapContext()
        {
            if (GxContext.Current == null)
            {
                var ctx = new GxContext();
                ctx.SoapContext = new GXSOAPContext();
            }
            return GxContext.Current.SoapContext;
        }
    }
}
