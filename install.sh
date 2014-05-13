configFile="installConfig.sh"

# check for installation config
if [ ! -e "$configFile" ]
then
        echo "Installation config not found: $configFile"
        exit
fi

source $configFile

XML_CONFIG_NAME="config.xml"
CONFIG_NAME="config.txt"

# check for server config
if [ ! -e "$XML_CONFIG_NAME" ]
then
    echo "static data delivery server config not found: \"$XML_CONFIG_NAME\""
    echo "edit \"sample-config.xml\" to match your needs and do"
    echo "cp sample-config.xml $XML_CONFIG_NAME"
    exit 1
fi

if [ ! -e "$CONFIG_NAME" ]
then
	echo "static data delivery server config not found: \"$CONFIG_NAME\""
	echo "edit \"sample-config.txt\" to match your needs and do"
	echo "cp sample-config.txt $CONFIG_NAME"
	exit 1
fi

echo "server directory is \"$SERVER_DIR\""
if [ ! -e "$SERVER_DIR" ]
then
	# create server directory
	echo "directory not present, creating..."
	sudo mkdir -p $SERVER_DIR
fi

# set directory rights
sudo chown -R $SERVER_DIR_RIGHTS $SERVER_DIR
echo "set directory rights to \"$SERVER_DIR_RIGHTS\""

# reset server files
echo "server directory is \"$SERVER_DIR\""

if [ "$SERVER_DIR" ]
then
	rm -rf /$SERVER_DIR/*
	echo "server directory cleaned"
fi

sudo cp $CONFIG_NAME $CONFIG_PATH
echo "server config is \"$CONFIG_PATH$CONFIG_NAME\""

sudo cp $XML_CONFIG_NAME $CONFIG_PATH
echo "server xml config is \"$CONFIG_PATH$XML_CONFIG_NAME\""
